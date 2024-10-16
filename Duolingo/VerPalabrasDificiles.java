package Duolingo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VerPalabrasDificiles extends JFrame {

    private int idEstudiante;
    
    private JButton btnAplicarFiltros, btnFinalizar;
    private JComboBox<String> cmbTipoPalabra, cmbDificultad;
    private JLabel lblFiltros, lblTipo, lblDificultad;
    private JTable tablePalabras;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    public VerPalabrasDificiles(int idEstudiante) {
        this.idEstudiante = idEstudiante;
        initComponents();
        cargarDatosIniciales();
    }

    private void initComponents() {
        setTitle("Ver Palabras Difíciles");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Establecer la fuente estándar
        Font standardFont = new Font("SansSerif", Font.PLAIN, 12);
        Font boldFont = new Font("SansSerif", Font.BOLD, 12);
        Font titleFont = new Font("SansSerif", Font.BOLD, 18);

        lblFiltros = new JLabel("Filtros");
        lblFiltros.setFont(titleFont);
        lblTipo = new JLabel("Tipo:");
        lblTipo.setFont(boldFont);
        lblDificultad = new JLabel("Dificultad:");
        lblDificultad.setFont(boldFont);

        cmbTipoPalabra = new JComboBox<>(new String[]{"Todos", "Sustantivo", "Verbo", "Adjetivo", "Preposición", "Artículo", "Pronombre"});
        cmbTipoPalabra.setFont(standardFont);
        cmbDificultad = new JComboBox<>(new String[]{"Todas", "Intermedio", "Difícil"});
        cmbDificultad.setFont(standardFont);

        btnAplicarFiltros = new JButton("Aplicar filtros");
        btnAplicarFiltros.setFont(standardFont);
        btnAplicarFiltros.setBackground(new Color(173, 216, 230));
        btnAplicarFiltros.addActionListener(this::btnAplicarFiltrosActionPerformed);

        btnFinalizar = new JButton("Finalizar");
        btnFinalizar.setFont(standardFont);
        btnFinalizar.setBackground(new Color(173, 216, 230));
        btnFinalizar.addActionListener(evt -> dispose());

        tableModel = new DefaultTableModel(new String[]{"Palabra", "Traducción", "Significado"}, 0);
        tablePalabras = new JTable(tableModel);
        scrollPane = new JScrollPane(tablePalabras);

        JPanel panelFiltros = new JPanel();
        panelFiltros.setBackground(Color.ORANGE);
        panelFiltros.add(lblFiltros);
        panelFiltros.add(lblTipo);
        panelFiltros.add(cmbTipoPalabra);
        panelFiltros.add(lblDificultad);
        panelFiltros.add(cmbDificultad);
        panelFiltros.add(btnAplicarFiltros);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.ORANGE);
        panelBotones.add(btnFinalizar);

        add(panelFiltros, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void cargarDatosIniciales() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            String query = "SELECT palabra.palabra, palabra.traduccion, palabra.significado FROM palabra " +
                    "JOIN estudiante_palabra ON palabra.id_palabra = estudiante_palabra.id_palabra " +
                    "WHERE estudiante_palabra.id_estudiante = ? AND palabra.id_dificultad = 3";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, idEstudiante);
                try (ResultSet rs = pstmt.executeQuery()) {
                    DefaultTableModel model = (DefaultTableModel) tablePalabras.getModel();
                    model.setRowCount(0); // Limpiar la tabla

                    while (rs.next()) {
                        String palabra = rs.getString("palabra");
                        String traduccion = rs.getString("traduccion");
                        String significado = rs.getString("significado");
                        model.addRow(new Object[]{palabra, traduccion, significado});
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void btnAplicarFiltrosActionPerformed(ActionEvent evt) {
        String tipoSeleccionado = cmbTipoPalabra.getSelectedItem().toString();
        String dificultadSeleccionada = cmbDificultad.getSelectedItem().toString();

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            StringBuilder query = new StringBuilder("SELECT palabra.palabra, palabra.traduccion, palabra.significado FROM palabra " +
                    "JOIN estudiante_palabra ON palabra.id_palabra = estudiante_palabra.id_palabra " +
                    "WHERE estudiante_palabra.id_estudiante = ? AND palabra.id_dificultad = 3");

            if (!tipoSeleccionado.equals("Todos")) {
                query.append(" AND palabra.id_tipo_palabra = ?");
            }

            try (PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
                pstmt.setInt(1, idEstudiante);

                int paramIndex = 2;
                if (!tipoSeleccionado.equals("Todos")) {
                    pstmt.setInt(paramIndex++, cmbTipoPalabra.getSelectedIndex());
                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    DefaultTableModel model = (DefaultTableModel) tablePalabras.getModel();
                    model.setRowCount(0); // Limpiar la tabla

                    while (rs.next()) {
                        String palabra = rs.getString("palabra");
                        String traduccion = rs.getString("traduccion");
                        String significado = rs.getString("significado");
                        model.addRow(new Object[]{palabra, traduccion, significado});
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        EventQueue.invokeLater(() -> {
            int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
            new VerPalabrasDificiles(idEstudiante).setVisible(true);
        });
    }
}
