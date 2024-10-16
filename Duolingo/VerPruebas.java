package Duolingo;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VerPruebas extends JFrame {
    private JTable table;
    private JScrollPane scrollPane;

    public VerPruebas(int idEstudiante) {
        setTitle("Pruebas Realizadas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        cargarPruebas(idEstudiante);

        setLocationRelativeTo(null);
    }

    private void cargarPruebas(int idEstudiante) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT p.id_prueba, p.fecha_prueba, p.porcentaje, d.dificultad AS dificultad " +
                         "FROM prueba p " +
                         "JOIN dificultad d ON p.id_dificultad = d.id_dificultad " +
                         "WHERE p.id_estudiante = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, idEstudiante);
            resultSet = statement.executeQuery();

            String[] columnNames = {"ID Prueba", "Fecha", "Porcentaje", "Dificultad"};
            Object[][] data = new Object[30][4]; // Ajustar el tamaño de la matriz según sea necesario
            int row = 0;

            while (resultSet.next()) {
                data[row][0] = resultSet.getInt("id_prueba");
                data[row][1] = resultSet.getDate("fecha_prueba");
                data[row][2] = resultSet.getDouble("porcentaje");
                data[row][3] = resultSet.getString("dificultad");
                row++;
            }

            table = new JTable(data, columnNames);
            table.getSelectionModel().addListSelectionListener(event -> {
                if (event.getValueIsAdjusting()) {
                    return;
                }
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int idPrueba = (int) table.getValueAt(selectedRow, 0);
                    verDetallePrueba(idPrueba);
                }
            });

            scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void verDetallePrueba(int idPrueba) {
        VerDetallePrueba verDetallePrueba = new VerDetallePrueba(idPrueba);
        verDetallePrueba.setVisible(true);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
            new VerPruebas(idEstudiante).setVisible(true);
        });
    }
}
