package Duolingo;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PracticarPronunciaciones extends JFrame {
    private JLabel lblTitulo, lblPalabra;
    private JButton btnSiguientePalabra, btnIniciarGrabacion, btnDetenerGrabacion, btnEscuchar, btnFinalizar;
    private List<String> palabras;
    private TargetDataLine targetLine;
    private AudioInputStream audioStream;
    private ByteArrayOutputStream out;
    private int idEstudiante;
    private Color originalColor;

    public PracticarPronunciaciones(int idEstudiante) {
        this.idEstudiante = idEstudiante;
        initComponents();
        cargarPalabras();
        mostrarPalabraAleatoria();
    }

    private void initComponents() {
        setTitle("Practicar Pronunciaciones");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        Font standardFont = new Font("SansSerif", Font.PLAIN, 12);
        Font boldFont = new Font("SansSerif", Font.BOLD, 12);
        Font titleFont = new Font("SansSerif", Font.BOLD, 18);

        lblTitulo = new JLabel("¡Es hora de practicar pronunciaciones!");
        lblTitulo.setFont(titleFont);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        lblPalabra = new JLabel("Palabra a practicar: **PALABRA ALEATORIA**");
        lblPalabra.setFont(boldFont);

        btnSiguientePalabra = new JButton("Siguiente palabra");
        btnSiguientePalabra.setFont(standardFont);
        btnSiguientePalabra.setBackground(new Color(173, 216, 230));

        btnIniciarGrabacion = new JButton("INICIAR GRABACIÓN");
        btnIniciarGrabacion.setFont(standardFont);
        btnIniciarGrabacion.setBackground(new Color(173, 216, 230));

        btnDetenerGrabacion = new JButton("DETENER GRABACIÓN");
        btnDetenerGrabacion.setFont(standardFont);
        btnDetenerGrabacion.setBackground(new Color(173, 216, 230));

        btnEscuchar = new JButton("Escuchar pronunciacion");
        btnEscuchar.setFont(standardFont);
        btnEscuchar.setBackground(new Color(173, 216, 230));

        btnFinalizar = new JButton("Finalizar");
        btnFinalizar.setFont(standardFont);
        btnFinalizar.setBackground(new Color(173, 216, 230));

        originalColor = btnIniciarGrabacion.getBackground();

        btnSiguientePalabra.addActionListener(evt -> mostrarPalabraAleatoria());
        btnIniciarGrabacion.addActionListener(evt -> grabarPronunciacion());
        btnDetenerGrabacion.addActionListener(evt -> detenerGrabacion());
        btnEscuchar.addActionListener(evt -> escucharPronunciacion());
        btnFinalizar.addActionListener(evt -> dispose());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        gbc.gridy = 1;
        add(lblPalabra, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(btnSiguientePalabra, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        add(btnIniciarGrabacion, gbc);
        gbc.gridx = 1;
        add(btnDetenerGrabacion, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        add(btnEscuchar, gbc);

        gbc.gridy = 5;
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
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT p.palabra FROM palabra p JOIN estudiante_palabra ep ON p.id_palabra = ep.id_palabra WHERE ep.id_estudiante = ?";
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
            btnSiguientePalabra.setEnabled(false);
        } else {
            Random random = new Random();
            String palabraAleatoria = palabras.get(random.nextInt(palabras.size()));
            lblPalabra.setText("Palabra a practicar: " + palabraAleatoria);
        }
    }

    private void grabarPronunciacion() {
        try {
            AudioFormat format = new AudioFormat(16000, 8, 2, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            targetLine = (TargetDataLine) AudioSystem.getLine(info);
            targetLine.open(format);
            targetLine.start();

            out = new ByteArrayOutputStream();
            Thread thread = new Thread(() -> {
                byte[] buffer = new byte[1024];
                while (targetLine.isOpen()) {
                    int bytesRead = targetLine.read(buffer, 0, buffer.length);
                    if (bytesRead > 0) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            });
            thread.start();
            btnIniciarGrabacion.setBackground(Color.GREEN);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void detenerGrabacion() {
        if (targetLine != null) {
            targetLine.stop();
            targetLine.close();
            audioStream = new AudioInputStream(new ByteArrayInputStream(out.toByteArray()), new AudioFormat(16000, 8, 2, true, true), out.size());
            btnIniciarGrabacion.setBackground(originalColor);
        }
    }

    private void escucharPronunciacion() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
            new PracticarPronunciaciones(idEstudiante).setVisible(true);
        });
    }
}
