package Duolingo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class VerOraciones extends JFrame {
    private JTable tableOraciones;
    private DefaultTableModel tableModel;
    private JButton btnBorrarOracion, btnFinalizar;
    private JComboBox<String> comboIdiomas;
    private int idEstudiante;
    private Connection connection;

    public VerOraciones(int idEstudiante) {
        this.idEstudiante = idEstudiante;
        initComponents();
        cargarIdiomas();
        cargarOraciones();
    }

    private void initComponents() {
        setTitle("Ver Oraciones");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Establecer la fuente estándar
        Font standardFont = new Font("SansSerif", Font.PLAIN, 12);
        Font boldFont = new Font("SansSerif", Font.BOLD, 12);
        Font titleFont = new Font("SansSerif", Font.BOLD, 18);

        tableModel = new DefaultTableModel(new String[]{"ID", "Oración"}, 0);
        tableOraciones = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableOraciones);

        btnBorrarOracion = new JButton("Borrar Oración");
        btnBorrarOracion.setFont(standardFont);
        btnBorrarOracion.setBackground(new Color(173, 216, 230));
        btnFinalizar = new JButton("Finalizar");
        btnFinalizar.setFont(standardFont);
        btnFinalizar.setBackground(new Color(173, 216, 230));
        comboIdiomas = new JComboBox<>();
        comboIdiomas.setFont(standardFont);
        comboIdiomas.addActionListener(e -> cargarOraciones());

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.ORANGE);
        panelBotones.add(new JLabel("Filtrar por idioma:"));
        panelBotones.add(comboIdiomas);
        panelBotones.add(btnBorrarOracion);
        panelBotones.add(btnFinalizar);

        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnBorrarOracion.addActionListener(e -> borrarOracion());
        btnFinalizar.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(null);
    }

    private void cargarIdiomas() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT idioma FROM idioma";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            comboIdiomas.addItem("Todos");
            while (resultSet.next()) {
                comboIdiomas.addItem(resultSet.getString("idioma"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarOraciones() {
        try {
            tableModel.setRowCount(0);
            connection = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT o.id_oracion, o.oracion " +
                         "FROM oracion o " +
                         "JOIN estudiante_oracion eo ON o.id_oracion = eo.id_oracion " +
                         "JOIN palabra p ON o.id_palabra = p.id_palabra " +
                         "JOIN idioma i ON p.id_idioma = i.id_idioma " +
                         "WHERE eo.id_estudiante = ?";
            if (!comboIdiomas.getSelectedItem().equals("Todos")) {
                sql += " AND i.idioma = ?";
            }
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idEstudiante);
            if (!comboIdiomas.getSelectedItem().equals("Todos")) {
                statement.setString(2, (String) comboIdiomas.getSelectedItem());
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idOracion = resultSet.getInt("id_oracion");
                String oracion = resultSet.getString("oracion");
                tableModel.addRow(new Object[]{idOracion, oracion});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void borrarOracion() {
        int selectedRow = tableOraciones.getSelectedRow();
        if (selectedRow != -1) {
            int idOracion = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                connection = DatabaseConnection.getInstance().getConnection();
                String sql = "DELETE FROM estudiante_oracion WHERE id_oracion = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, idOracion);
                statement.executeUpdate();

                sql = "DELETE FROM oracion WHERE id_oracion = ?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, idOracion);
                statement.executeUpdate();

                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Oración borrada con éxito.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una oración para borrar.");
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
            new VerOraciones(idEstudiante).setVisible(true);
        });
    }
}
