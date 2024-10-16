package Duolingo;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PracticarPalabrasRegistradas extends JFrame {
    private JLabel lblTitulo, lblPalabra, lblTraduccion;
    private JButton btnPalabraAprendida, btnSiguientePalabra, btnFinalizar;
    private List<String[]> palabras;
    private int idEstudiante;
    private Connection connection;

    public PracticarPalabrasRegistradas(int idEstudiante) {
        this.idEstudiante = idEstudiante;
        initComponents();
        cargarPalabras();
        mostrarPalabraAleatoria();
    }

    private void initComponents() {
        setTitle("Practicar Palabras Registradas");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        // Establecer la fuente estándar
        Font standardFont = new Font("SansSerif", Font.PLAIN, 12);
        Font boldFont = new Font("SansSerif", Font.BOLD, 12);
        Font titleFont = new Font("SansSerif", Font.BOLD, 18);

        lblTitulo = new JLabel("¡Es hora de practicar!");
        lblTitulo.setFont(titleFont);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        lblPalabra = new JLabel("Palabra:");
        lblPalabra.setFont(boldFont);
        lblTraduccion = new JLabel("Traducción:");
        lblTraduccion.setFont(standardFont);

        btnPalabraAprendida = new JButton("Palabra Aprendida");
        btnPalabraAprendida.setFont(standardFont);
        btnPalabraAprendida.setBackground(new Color(173, 216, 230));
        btnSiguientePalabra = new JButton("Siguiente Palabra");
        btnSiguientePalabra.setFont(standardFont);
        btnSiguientePalabra.setBackground(new Color(173, 216, 230));
        btnFinalizar = new JButton("Finalizar");
        btnFinalizar.setFont(standardFont);
        btnFinalizar.setBackground(new Color(173, 216, 230));

        btnPalabraAprendida.addActionListener(evt -> marcarPalabraComoAprendida());
        btnSiguientePalabra.addActionListener(evt -> mostrarPalabraAleatoria());
        btnFinalizar.addActionListener(evt -> dispose());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        gbc.gridy = 1;
        add(lblPalabra, gbc);

        gbc.gridy = 2;
        add(lblTraduccion, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(btnPalabraAprendida, gbc);

        gbc.gridx = 1;
        add(btnSiguientePalabra, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(btnFinalizar, gbc);

        getContentPane().setBackground(Color.ORANGE);

        pack();
        setLocationRelativeTo(null);
    }

    private void cargarPalabras() {
        palabras = new ArrayList<>();
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT p.id_palabra, p.palabra, p.traduccion " +
                         "FROM palabra p " +
                         "JOIN estudiante_palabra ep ON p.id_palabra = ep.id_palabra " +
                         "WHERE ep.id_estudiante = ? AND ep.id_estado = 2 AND p.id_dificultad != 3";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idEstudiante);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idPalabra = resultSet.getInt("id_palabra");
                String palabra = resultSet.getString("palabra");
                String traduccion = resultSet.getString("traduccion");
                palabras.add(new String[]{String.valueOf(idPalabra), palabra, traduccion});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarPalabraAleatoria() {
        if (palabras.isEmpty()) {
            lblPalabra.setText("No hay palabras registradas.");
            lblTraduccion.setText("");
            btnPalabraAprendida.setEnabled(false);
            btnSiguientePalabra.setEnabled(false);
        } else {
            Random random = new Random();
            String[] palabraAleatoria = palabras.get(random.nextInt(palabras.size()));
            lblPalabra.setText(palabraAleatoria[1]);
            lblTraduccion.setText(palabraAleatoria[2]);
        }
    }

    private void marcarPalabraComoAprendida() {
        try {
            String palabra = lblPalabra.getText();
            String sql = "UPDATE estudiante_palabra SET id_estado = 1 " +
                         "WHERE id_palabra = (SELECT id_palabra FROM palabra WHERE palabra = ?) " +
                         "AND id_estudiante = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, palabra);
            statement.setInt(2, idEstudiante);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Palabra marcada como aprendida.");
            mostrarPalabraAleatoria();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
            new PracticarPalabrasRegistradas(idEstudiante).setVisible(true);
        });
    }
}
