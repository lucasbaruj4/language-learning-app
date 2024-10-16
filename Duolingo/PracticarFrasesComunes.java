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

public class PracticarFrasesComunes extends JFrame {
    private JLabel lblTitulo, lblFrase, lblTraduccion;
    private JButton btnFraseAprendida, btnSiguienteFrase, btnFinalizar;
    private List<String[]> frases;
    private int idEstudiante;
    private Connection connection;

    public PracticarFrasesComunes(int idEstudiante) {
        this.idEstudiante = idEstudiante;
        initComponents();
        cargarFrases();
        mostrarFraseAleatoria();
    }

    private void initComponents() {
        setTitle("Practicar Frases Comunes");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        Font standardFont = new Font("SansSerif", Font.PLAIN, 12);
        Font boldFont = new Font("SansSerif", Font.BOLD, 12);
        Font titleFont = new Font("SansSerif", Font.BOLD, 18);

        lblTitulo = new JLabel("¡Es hora de practicar frases comunes!");
        lblTitulo.setFont(titleFont);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        lblFrase = new JLabel("Frase:");
        lblFrase.setFont(boldFont);

        lblTraduccion = new JLabel("Traducción:");
        lblTraduccion.setFont(standardFont);

        btnFraseAprendida = new JButton("Frase Aprendida");
        btnFraseAprendida.setFont(standardFont);
        btnFraseAprendida.setBackground(new Color(173, 216, 230));

        btnSiguienteFrase = new JButton("Siguiente Frase");
        btnSiguienteFrase.setFont(standardFont);
        btnSiguienteFrase.setBackground(new Color(173, 216, 230));

        btnFinalizar = new JButton("Finalizar");
        btnFinalizar.setFont(standardFont);
        btnFinalizar.setBackground(new Color(173, 216, 230));

        btnFraseAprendida.addActionListener(evt -> marcarFraseComoAprendida());
        btnSiguienteFrase.addActionListener(evt -> mostrarFraseAleatoria());
        btnFinalizar.addActionListener(evt -> dispose());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        gbc.gridy = 1;
        add(lblFrase, gbc);

        gbc.gridy = 2;
        add(lblTraduccion, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(btnFraseAprendida, gbc);

        gbc.gridx = 1;
        add(btnSiguienteFrase, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(btnFinalizar, gbc);

        getContentPane().setBackground(Color.ORANGE);

        pack();
        setLocationRelativeTo(null);
    }

    private void cargarFrases() {
        frases = new ArrayList<>();
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT id_frase, frase, traduccion FROM frase WHERE id_tipo_frase = 2";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idFrase = resultSet.getInt("id_frase");
                String frase = resultSet.getString("frase");
                String traduccion = resultSet.getString("traduccion");
                frases.add(new String[]{String.valueOf(idFrase), frase, traduccion});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarFraseAleatoria() {
        if (frases.isEmpty()) {
            lblFrase.setText("No hay frases comunes registradas.");
            lblTraduccion.setText("");
            btnFraseAprendida.setEnabled(false);
            btnSiguienteFrase.setEnabled(false);
        } else {
            Random random = new Random();
            String[] fraseAleatoria = frases.get(random.nextInt(frases.size()));
            lblFrase.setText(fraseAleatoria[1]);
            lblTraduccion.setText(fraseAleatoria[2]);
        }
    }

    private void marcarFraseComoAprendida() {
        try {
            String frase = lblFrase.getText();
            String sql = "INSERT INTO estudiante_frase (id_estudiante, id_frase, id_estado) " +
                         "VALUES (?, (SELECT id_frase FROM frase WHERE frase = ?), 1)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idEstudiante);
            statement.setString(2, frase);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Frase marcada como aprendida.");
            mostrarFraseAleatoria();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
            new PracticarFrasesComunes(idEstudiante).setVisible(true);
        });
    }
}
