package Duolingo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VerPalabrasAprendidas extends JFrame {
    private JTable tablePalabras;
    private DefaultTableModel tableModel;
    private JButton btnBorrarPalabra, btnFinalizar;
    private JComboBox<String> comboTipoPalabra, comboIdioma, comboDificultad;
    private int idEstudiante;
    private Connection connection;

    public VerPalabrasAprendidas(int idEstudiante) {
        this.idEstudiante = idEstudiante;
        initComponents();
        cargarFiltros();
        cargarPalabras();
    }

    private void initComponents() {
        setTitle("Ver Palabras Aprendidas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Establecer la fuente estándar
        Font standardFont = new Font("SansSerif", Font.PLAIN, 12);
        Font boldFont = new Font("SansSerif", Font.BOLD, 12);
        Font titleFont = new Font("SansSerif", Font.BOLD, 18);

        tableModel = new DefaultTableModel(new String[]{"Palabra", "Traducción"}, 0);
        tablePalabras = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablePalabras);

        btnBorrarPalabra = new JButton("Borrar Palabra");
        btnBorrarPalabra.setFont(standardFont);
        btnBorrarPalabra.setBackground(new Color(173, 216, 230));
        btnFinalizar = new JButton("Finalizar");
        btnFinalizar.setFont(standardFont);
        btnFinalizar.setBackground(new Color(173, 216, 230));

        comboTipoPalabra = new JComboBox<>();
        comboTipoPalabra.setFont(standardFont);
        comboIdioma = new JComboBox<>();
        comboIdioma.setFont(standardFont);
        comboDificultad = new JComboBox<>();
        comboDificultad.setFont(standardFont);

        comboTipoPalabra.addActionListener(e -> cargarPalabras());
        comboIdioma.addActionListener(e -> cargarPalabras());
        comboDificultad.addActionListener(e -> cargarPalabras());

        JPanel panelFiltros = new JPanel();
        panelFiltros.setBackground(Color.ORANGE);
        panelFiltros.add(new JLabel("Filtrar por tipo de palabra:"));
        panelFiltros.add(comboTipoPalabra);
        panelFiltros.add(new JLabel("Filtrar por idioma:"));
        panelFiltros.add(comboIdioma);
        panelFiltros.add(new JLabel("Filtrar por dificultad:"));
        panelFiltros.add(comboDificultad);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.ORANGE);
        panelBotones.add(btnBorrarPalabra);
        panelBotones.add(btnFinalizar);

        add(panelFiltros, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnBorrarPalabra.addActionListener(e -> borrarPalabra());
        btnFinalizar.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(null);
    }

    private void cargarFiltros() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();

            String sqlTipoPalabra = "SELECT tipo_palabra FROM tipo_palabra";
            PreparedStatement statementTipoPalabra = connection.prepareStatement(sqlTipoPalabra);
            ResultSet resultSetTipoPalabra = statementTipoPalabra.executeQuery();
            comboTipoPalabra.addItem("Todos");
            while (resultSetTipoPalabra.next()) {
                comboTipoPalabra.addItem(resultSetTipoPalabra.getString("tipo_palabra"));
            }

            String sqlIdioma = "SELECT idioma FROM idioma";
            PreparedStatement statementIdioma = connection.prepareStatement(sqlIdioma);
            ResultSet resultSetIdioma = statementIdioma.executeQuery();
            comboIdioma.addItem("Todos");
            while (resultSetIdioma.next()) {
                comboIdioma.addItem(resultSetIdioma.getString("idioma"));
            }

            String sqlDificultad = "SELECT dificultad FROM dificultad";
            PreparedStatement statementDificultad = connection.prepareStatement(sqlDificultad);
            ResultSet resultSetDificultad = statementDificultad.executeQuery();
            comboDificultad.addItem("Todos");
            while (resultSetDificultad.next()) {
                comboDificultad.addItem(resultSetDificultad.getString("dificultad"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarPalabras() {
        try {
            tableModel.setRowCount(0);
            connection = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT p.palabra, p.traduccion " +
                    "FROM palabra p " +
                    "JOIN estudiante_palabra ep ON p.id_palabra = ep.id_palabra " +
                    "JOIN tipo_palabra tp ON p.id_tipo_palabra = tp.id_tipo_palabra " +
                    "JOIN idioma i ON p.id_idioma = i.id_idioma " +
                    "JOIN dificultad d ON p.id_dificultad = d.id_dificultad " +
                    "WHERE ep.id_estudiante = ? AND ep.id_estado = 1";
            List<String> filters = new ArrayList<>();
            if (comboTipoPalabra.getSelectedItem() != null && !comboTipoPalabra.getSelectedItem().equals("Todos")) {
                filters.add("tp.tipo_palabra = ?");
            }
            if (comboIdioma.getSelectedItem() != null && !comboIdioma.getSelectedItem().equals("Todos")) {
                filters.add("i.idioma = ?");
            }
            if (comboDificultad.getSelectedItem() != null && !comboDificultad.getSelectedItem().equals("Todos")) {
                filters.add("d.dificultad = ?");
            }

            if (!filters.isEmpty()) {
                sql += " AND " + String.join(" AND ", filters);
            }

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idEstudiante);
            int index = 2;
            if (comboTipoPalabra.getSelectedItem() != null && !comboTipoPalabra.getSelectedItem().equals("Todos")) {
                statement.setString(index++, (String) comboTipoPalabra.getSelectedItem());
            }
            if (comboIdioma.getSelectedItem() != null && !comboIdioma.getSelectedItem().equals("Todos")) {
                statement.setString(index++, (String) comboIdioma.getSelectedItem());
            }
            if (comboDificultad.getSelectedItem() != null && !comboDificultad.getSelectedItem().equals("Todos")) {
                statement.setString(index++, (String) comboDificultad.getSelectedItem());
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String palabra = resultSet.getString("palabra");
                String traduccion = resultSet.getString("traduccion");
                tableModel.addRow(new Object[]{palabra, traduccion});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void borrarPalabra() {
        int selectedRow = tablePalabras.getSelectedRow();
        if (selectedRow != -1) {
            String palabra = (String) tableModel.getValueAt(selectedRow, 0);
            try {
                connection = DatabaseConnection.getInstance().getConnection();
                String sql = "DELETE FROM estudiante_palabra WHERE id_palabra = (SELECT id_palabra FROM palabra WHERE palabra = ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, palabra);
                statement.executeUpdate();

                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Palabra borrada con éxito.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una palabra para borrar.");
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
            new VerPalabrasAprendidas(idEstudiante).setVisible(true);
        });
    }
}
