package Duolingo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class VerFrasesComunes extends JFrame {
    private JTable tableFrases;
    private DefaultTableModel tableModel;
    private JButton btnBorrarFrase, btnFinalizar;
    private JComboBox<String> comboIdiomas;
    private int idEstudiante;
    private Connection connection;

    public VerFrasesComunes(int idEstudiante) {
        this.idEstudiante = idEstudiante;
        initComponents();
        cargarIdiomas();
        cargarFrases();
    }

    private void initComponents() {
        setTitle("Ver Frases Comunes Aprendidas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Establecer la fuente estándar
        Font standardFont = new Font("SansSerif", Font.PLAIN, 12);
        Font boldFont = new Font("SansSerif", Font.BOLD, 12);
        Font titleFont = new Font("SansSerif", Font.BOLD, 18);

        tableModel = new DefaultTableModel(new String[]{"Frase", "Traducción"}, 0);
        tableFrases = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableFrases);

        btnBorrarFrase = new JButton("Borrar Frase");
        btnBorrarFrase.setFont(standardFont);
        btnBorrarFrase.setBackground(new Color(173, 216, 230));
        btnFinalizar = new JButton("Finalizar");
        btnFinalizar.setFont(standardFont);
        btnFinalizar.setBackground(new Color(173, 216, 230));
        comboIdiomas = new JComboBox<>();
        comboIdiomas.setFont(standardFont);
        comboIdiomas.addActionListener(e -> cargarFrases());

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.ORANGE);
        panelBotones.add(new JLabel("Filtrar por idioma:"));
        panelBotones.add(comboIdiomas);
        panelBotones.add(btnBorrarFrase);
        panelBotones.add(btnFinalizar);

        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnBorrarFrase.addActionListener(e -> borrarFrase());
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

    private void cargarFrases() {
        try {
            tableModel.setRowCount(0);
            connection = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT f.frase, f.traduccion " +
                    "FROM frase f " +
                    "JOIN estudiante_frase ef ON f.id_frase = ef.id_frase " +
                    "JOIN idioma i ON f.id_idioma = i.id_idioma " +
                    "WHERE ef.id_estudiante = ? AND f.id_tipo_frase = 2";
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
                String frase = resultSet.getString("frase");
                String traduccion = resultSet.getString("traduccion");
                tableModel.addRow(new Object[]{frase, traduccion});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void borrarFrase() {
        int selectedRow = tableFrases.getSelectedRow();
        if (selectedRow != -1) {
            String frase = (String) tableModel.getValueAt(selectedRow, 0);
            try {
                connection = DatabaseConnection.getInstance().getConnection();
                String sql = "DELETE FROM estudiante_frase WHERE id_frase = (SELECT id_frase FROM frase WHERE frase = ? AND id_tipo_frase = 2)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, frase);
                statement.executeUpdate();

                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Frase borrada con éxito.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una frase para borrar.");
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
            new VerFrasesComunes(idEstudiante).setVisible(true);
        });
    }
}
