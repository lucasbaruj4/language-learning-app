package Duolingo;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VerDetallePrueba extends JFrame {
    private JTable table;
    private JScrollPane scrollPane;

    public VerDetallePrueba(int idPrueba) {
        setTitle("Detalle de la Prueba");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        cargarDetallePrueba(idPrueba);

        setLocationRelativeTo(null);
    }

    private void cargarDetallePrueba(int idPrueba) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT p.palabra, pp.respuesta_elegida, p.traduccion AS respuesta_correcta " +
                         "FROM prueba_palabras pp " +
                         "JOIN palabra p ON pp.id_palabra = p.id_palabra " +
                         "WHERE pp.id_prueba = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, idPrueba);
            resultSet = statement.executeQuery();

            String[] columnNames = {"Palabra", "Respuesta Elegida", "Respuesta Correcta"};
            Object[][] data = new Object[30][3]; // Ajustar el tamaño de la matriz según sea necesario
            int row = 0;

            while (resultSet.next()) {
                data[row][0] = resultSet.getString("palabra");
                data[row][1] = resultSet.getString("respuesta_elegida");
                data[row][2] = resultSet.getString("respuesta_correcta");
                row++;
            }

            table = new JTable(data, columnNames);
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

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new VerDetallePrueba(1).setVisible(true); // Pasar un idPrueba válido para pruebas
        });
    }
}
