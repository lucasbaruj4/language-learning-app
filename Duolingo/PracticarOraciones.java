package Duolingo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PracticarOraciones extends JFrame {
    private JLabel lblTitulo, lblPalabra;
    private JTextField txtOracion;
    private JButton btnRegistrarOracion, btnFinalizar;
    private List<String> palabras;
    private int idEstudiante;
    private Connection connection;

    public PracticarOraciones(int idEstudiante) {
        this.idEstudiante = idEstudiante;
        initComponents();
        cargarPalabras();
        mostrarPalabraAleatoria();
    }

    private void initComponents() {
        setTitle("Practicar Oraciones");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        Font standardFont = new Font("SansSerif", Font.PLAIN, 12);
        Font boldFont = new Font("SansSerif", Font.BOLD, 12);
        Font titleFont = new Font("SansSerif", Font.BOLD, 18);

        lblTitulo = new JLabel("¡Es hora de practicar oraciones!");
        lblTitulo.setFont(titleFont);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        lblPalabra = new JLabel("Palabra:");
        lblPalabra.setFont(boldFont);

        txtOracion = new JTextField(20);
        txtOracion.setFont(standardFont);

        btnRegistrarOracion = new JButton("Registrar Oración");
        btnRegistrarOracion.setFont(standardFont);
        btnRegistrarOracion.setBackground(new Color(173, 216, 230));

        btnFinalizar = new JButton("Finalizar");
        btnFinalizar.setFont(standardFont);
        btnFinalizar.setBackground(new Color(173, 216, 230));

        btnRegistrarOracion.addActionListener(evt -> registrarOracion());
        btnFinalizar.addActionListener(evt -> dispose());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        gbc.gridy = 1;
        add(lblPalabra, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(txtOracion, gbc);

        gbc.gridy = 3;
        add(btnRegistrarOracion, gbc);

        gbc.gridy = 4;
        add(btnFinalizar, gbc);

        getContentPane().setBackground(Color.ORANGE);

        pack();
        setLocationRelativeTo(null);
    }

    private void cargarPalabras() {
        palabras = new ArrayList<>();
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT p.palabra FROM Palabra p JOIN Estudiante_Palabra ep ON p.id_palabra = ep.id_palabra WHERE ep.id_estudiante = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idEstudiante);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                palabras.add(resultSet.getString("palabra"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarPalabraAleatoria() {
        if (palabras.isEmpty()) {
            lblPalabra.setText("No hay palabras registradas.");
            btnRegistrarOracion.setEnabled(false);
        } else {
            Random random = new Random();
            String palabraAleatoria = palabras.get(random.nextInt(palabras.size()));
            lblPalabra.setText("Palabra: " + palabraAleatoria);
        }
    }

    private void registrarOracion() {
        String oracion = txtOracion.getText().trim();
        if (oracion.length() >= 6 && !oracion.isEmpty()) {
            try {
                String palabra = lblPalabra.getText().replace("Palabra: ", "");
                String sqlOracion = "INSERT INTO Oracion (oracion, id_palabra) VALUES (?, (SELECT id_palabra FROM Palabra WHERE palabra = ? LIMIT 1)) RETURNING id_oracion";
                PreparedStatement statementOracion = connection.prepareStatement(sqlOracion);
                statementOracion.setString(1, oracion);
                statementOracion.setString(2, palabra);
                ResultSet rs = statementOracion.executeQuery();
                int idOracion = -1;
                if (rs.next()) {
                    idOracion = rs.getInt("id_oracion");
                }

                if (idOracion != -1) {
                    String sqlRelacion = "INSERT INTO Estudiante_Oracion (id_estudiante, id_oracion) VALUES (?, ?)";
                    PreparedStatement statementRelacion = connection.prepareStatement(sqlRelacion);
                    statementRelacion.setInt(1, idEstudiante);
                    statementRelacion.setInt(2, idOracion);
                    statementRelacion.executeUpdate();
                }

                JOptionPane.showMessageDialog(this, "Oración registrada con éxito.");
                txtOracion.setText("");
                mostrarPalabraAleatoria();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "La oración debe tener al menos 6 caracteres.");
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
            new PracticarOraciones(idEstudiante).setVisible(true);
        });
    }
}
