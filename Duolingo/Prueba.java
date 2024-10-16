package Duolingo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Prueba extends JFrame {
    private JLabel lblPalabra, lblInstrucciones, lblPregunta;
    private JButton btnOpcion1, btnOpcion2, btnOpcion3, btnOpcion4, btnFinalizar;
    private JComboBox<String> cmbDificultad;
    private List<String[]> palabras;
    private String[] palabraActual;
    private int idEstudiante, idPrueba, palabrasCorrectas, palabrasTotales;
    private Connection connection;

    public Prueba(int idEstudiante) {
        this.idEstudiante = idEstudiante;
        initComponents();
    }

    private void initComponents() {
        setTitle("Prueba de Palabras");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.ORANGE); // Fondo naranja
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        lblInstrucciones = new JLabel("<html>La temática de la prueba es la siguiente:<br>Te aparecerá una palabra y tendrás 4 opciones con las posibles traducciones.<br>Selecciona la traducción correcta.<br>Por favor, elige la dificultad:</html>");
        lblInstrucciones.setFont(new Font("Arial", Font.PLAIN, 16)); // Fuente mejorada
        lblPregunta = new JLabel("Dificultad:");
        lblPregunta.setFont(new Font("Arial", Font.BOLD, 16));
        cmbDificultad = new JComboBox<>(new String[]{"Fácil", "Intermedio", "Difícil"});
        btnFinalizar = new JButton("Iniciar Prueba");
        btnFinalizar.addActionListener(evt -> iniciarPrueba());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblInstrucciones, gbc);

        gbc.gridy = 1;
        add(lblPregunta, gbc);

        gbc.gridy = 2;
        add(cmbDificultad, gbc);

        gbc.gridy = 3;
        add(btnFinalizar, gbc);

        pack();
        setLocationRelativeTo(null);
    }

    private void iniciarPrueba() {
        String dificultad = (String) cmbDificultad.getSelectedItem();
        palabrasTotales = dificultad.equals("Fácil") ? 10 : dificultad.equals("Intermedio") ? 20 : 30;

        cargarPalabras();
        if (palabras.size() < 4) {
            JOptionPane.showMessageDialog(this, "No hay suficientes palabras registradas para realizar la prueba. Se necesitan al menos 4 palabras.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        registrarPrueba();
        configurarPrueba();
        mostrarPalabraAleatoria();
    }

    private void cargarPalabras() {
        palabras = new ArrayList<>();
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT p.id_palabra, p.palabra, p.traduccion " +
                         "FROM palabra p " +
                         "JOIN estudiante_palabra ep ON p.id_palabra = ep.id_palabra " +
                         "WHERE ep.id_estudiante = ? AND ep.id_estado = 2";
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

    private void registrarPrueba() {
        try {
            String sql = "INSERT INTO prueba (id_idioma, id_estudiante, fecha_prueba, id_dificultad) VALUES (?, ?, CURRENT_DATE, ?) RETURNING id_prueba";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, 1); // Cambiar según el idioma seleccionado
            statement.setInt(2, idEstudiante);
            statement.setInt(3, cmbDificultad.getSelectedIndex() + 1);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                idPrueba = resultSet.getInt("id_prueba");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void configurarPrueba() {
        getContentPane().removeAll();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        lblPalabra = new JLabel("Palabra:");
        lblPalabra.setFont(new Font("Arial", Font.BOLD, 24));

        btnOpcion1 = new JButton();
        btnOpcion2 = new JButton();
        btnOpcion3 = new JButton();
        btnOpcion4 = new JButton();
        btnFinalizar = new JButton("Finalizar Prueba");

        btnOpcion1.setPreferredSize(new Dimension(200, 50));
        btnOpcion2.setPreferredSize(new Dimension(200, 50));
        btnOpcion3.setPreferredSize(new Dimension(200, 50));
        btnOpcion4.setPreferredSize(new Dimension(200, 50));

        btnOpcion1.addActionListener(this::verificarRespuesta);
        btnOpcion2.addActionListener(this::verificarRespuesta);
        btnOpcion3.addActionListener(this::verificarRespuesta);
        btnOpcion4.addActionListener(this::verificarRespuesta);
        btnFinalizar.addActionListener(evt -> finalizarPrueba());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblPalabra, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(btnOpcion1, gbc);

        gbc.gridx = 1;
        add(btnOpcion2, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        add(btnOpcion3, gbc);

        gbc.gridx = 1;
        add(btnOpcion4, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(btnFinalizar, gbc);

        pack();
        setLocationRelativeTo(null);
    }

    private void mostrarPalabraAleatoria() {
        if (palabras.isEmpty()) {
            finalizarPrueba();
            return;
        }

        Random random = new Random();
        palabraActual = palabras.remove(random.nextInt(palabras.size()));
        lblPalabra.setText("Palabra: " + palabraActual[1]);

        List<String> opciones = new ArrayList<>();
        opciones.add(palabraActual[2]);
        while (opciones.size() < 4) {
            String[] palabraAleatoria = palabras.get(random.nextInt(palabras.size()));
            if (!opciones.contains(palabraAleatoria[2])) {
                opciones.add(palabraAleatoria[2]);
            }
        }
        Collections.shuffle(opciones);

        btnOpcion1.setText(opciones.get(0));
        btnOpcion2.setText(opciones.get(1));
        btnOpcion3.setText(opciones.get(2));
        btnOpcion4.setText(opciones.get(3));
    }

    private void verificarRespuesta(ActionEvent evt) {
        JButton botonSeleccionado = (JButton) evt.getSource();
        try {
            String sql = "INSERT INTO prueba_palabras (id_prueba, id_palabra, respuesta_elegida, id_estudiante, id_dificultad) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idPrueba);
            statement.setInt(2, Integer.parseInt(palabraActual[0]));
            statement.setString(3, botonSeleccionado.getText());
            statement.setInt(4, idEstudiante);
            statement.setInt(5, cmbDificultad.getSelectedIndex() + 1);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (botonSeleccionado.getText().equals(palabraActual[2])) {
            JOptionPane.showMessageDialog(this, "¡Bien hecho!", "Correcto", JOptionPane.INFORMATION_MESSAGE);
            palabrasCorrectas++;
        } else {
            JOptionPane.showMessageDialog(this, "Sigue intentándolo, ¡tú puedes!", "Incorrecto", JOptionPane.ERROR_MESSAGE);
        }

        if (palabrasTotales - (palabrasTotales - palabras.size()) == 0) {
            finalizarPrueba();
        } else {
            mostrarPalabraAleatoria();
        }
    }

    private void finalizarPrueba() {
        double porcentaje = ((double) palabrasCorrectas / palabrasTotales) * 100;

        try {
            String sql = "UPDATE prueba SET porcentaje = ? WHERE id_prueba = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, porcentaje);
            statement.setInt(2, idPrueba);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(this, "Prueba finalizada.\nTu porcentaje de aciertos es: " + porcentaje + "%", "Resultado", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
            new Prueba(idEstudiante).setVisible(true);
        });
    }
}
