package Duolingo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Configuracion extends JFrame {

    private JTextField txtNombre, txtApellido, txtUsuario;
    private JComboBox<String> comboIdioma, comboPrivacidad;
    private JButton btnAplicar, btnVolver, btnCambiarContrasena;
    private int idEstudiante;
    private Connection connection;

    public Configuracion(int idEstudiante) {
        this.idEstudiante = idEstudiante;
        initComponents();
        cargarIdiomas();
        cargarDatosEstudiante();
    }

    private void initComponents() {
        setTitle("Configuración");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.ORANGE); // Fondo naranja
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        setLocationRelativeTo(null);

        JLabel lblConfiguracion = new JLabel("Configuración");
        lblConfiguracion.setFont(new Font("Arial", Font.BOLD, 20)); // Fuente mejorada
        JLabel lblNombre = new JLabel("Cambiar nombre:");
        JLabel lblApellido = new JLabel("Cambiar apellido:");
        JLabel lblUsuario = new JLabel("Cambiar nombre de usuario:");
        JLabel lblPrivacidad = new JLabel("Configuración de privacidad:");
        JLabel lblIdioma = new JLabel("Idioma predeterminado:");

        txtNombre = new JTextField(15);
        txtApellido = new JTextField(15);
        txtUsuario = new JTextField(15);

        comboPrivacidad = new JComboBox<>(new String[]{"Público", "Privado"});
        comboIdioma = new JComboBox<>();

        btnAplicar = new JButton("Aplicar");
        btnVolver = new JButton("Volver");
        btnCambiarContrasena = new JButton("Cambiar contraseña");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblConfiguracion, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblNombre, gbc);
        gbc.gridx = 1;
        add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblApellido, gbc);
        gbc.gridx = 1;
        add(txtApellido, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(lblUsuario, gbc);
        gbc.gridx = 1;
        add(txtUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(lblPrivacidad, gbc);
        gbc.gridx = 1;
        add(comboPrivacidad, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(lblIdioma, gbc);
        gbc.gridx = 1;
        add(comboIdioma, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(btnCambiarContrasena, gbc);
        gbc.gridx = 1;
        add(btnAplicar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnVolver, gbc);

        btnAplicar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarCambios();
            }
        });

        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnCambiarContrasena.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
                new CambiarContraseña(idEstudiante).setVisible(true);
            }
        });
    }

    private void cargarIdiomas() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT idioma FROM Idioma";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                comboIdioma.addItem(resultSet.getString("idioma"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarDatosEstudiante() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT nombre, apellido, usuario, id_idioma FROM Estudiante WHERE id_estudiante = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idEstudiante);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                txtNombre.setText(resultSet.getString("nombre"));
                txtApellido.setText(resultSet.getString("apellido"));
                txtUsuario.setText(resultSet.getString("usuario"));
                comboIdioma.setSelectedIndex(resultSet.getInt("id_idioma") - 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void aplicarCambios() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas realizar estos cambios?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                connection = DatabaseConnection.getInstance().getConnection();
                String sql = "UPDATE Estudiante SET nombre = ?, apellido = ?, usuario = ?, id_idioma = ? WHERE id_estudiante = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, txtNombre.getText());
                statement.setString(2, txtApellido.getText());
                statement.setString(3, txtUsuario.getText());
                statement.setInt(4, comboIdioma.getSelectedIndex() + 1);
                statement.setInt(5, idEstudiante);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Cambios aplicados con éxito.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
                new Configuracion(idEstudiante).setVisible(true);
            }
        });
    }
}
