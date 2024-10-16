package Duolingo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OlvidarContraseña extends JFrame {

    private int intentosFallidos = 0;
    private static final int MAX_INTENTOS = 3;

    private JTextField txtUsuario, txtPalabraSecreta;
    private JButton btnSiguiente, btnVolver;
    private JLabel lblTitulo, lblUsuario, lblPalabraSecreta;

    public OlvidarContraseña() {
        initComponents();
    }

    private void initComponents() {
        setTitle("¿Olvidaste tu contraseña?");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        lblTitulo = new JLabel("¿Olvidaste tu contraseña?");
        lblUsuario = new JLabel("Ingrese su usuario:");
        lblPalabraSecreta = new JLabel("Ingrese su palabra secreta:");

        txtUsuario = new JTextField(15);
        txtPalabraSecreta = new JTextField(15);

        btnSiguiente = new JButton("Siguiente");
        btnVolver = new JButton("Volver");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitulo, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblUsuario, gbc);
        gbc.gridx = 1;
        add(txtUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblPalabraSecreta, gbc);
        gbc.gridx = 1;
        add(txtPalabraSecreta, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(btnSiguiente, gbc);
        gbc.gridx = 1;
        add(btnVolver, gbc);

        btnSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });

        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private void btnSiguienteActionPerformed(ActionEvent evt) {
        String usuario = txtUsuario.getText();
        String palabraSecreta = txtPalabraSecreta.getText();

        // Validar que los campos no estén vacíos
        if (usuario.isEmpty() || palabraSecreta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ambos campos deben contener caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int idEstudiante = verificarUsuarioYPreguntaSecreta(usuario, palabraSecreta);
            if (idEstudiante != -1) {
                // Guardar el ID del estudiante en la sesión
                SesionEstudiante.getInstance().iniciarSesion(idEstudiante, null); // Iniciar sesión sin nombre de usuario

                // Ir a CambiarContraseña
                CambiarContraseña cambiarContraseña = new CambiarContraseña(idEstudiante);
                cambiarContraseña.setVisible(true);
                this.dispose(); // Cierra la pantalla actual
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int verificarUsuarioYPreguntaSecreta(String usuario, String palabraSecreta) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT id_estudiante, palabra_secreta FROM estudiante WHERE usuario = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, usuario);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPalabraSecreta = rs.getString("palabra_secreta");
                if (storedPalabraSecreta.equals(palabraSecreta)) {
                    return rs.getInt("id_estudiante");
                } else {
                    intentosFallidos++;
                    if (intentosFallidos >= MAX_INTENTOS) {
                        JOptionPane.showMessageDialog(this, "Ha excedido el número máximo de intentos.", "Error", JOptionPane.ERROR_MESSAGE);
                        btnSiguiente.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(this, "Palabra secreta incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    return -1;
                }
            } else {
                JOptionPane.showMessageDialog(this, "El usuario no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                return -1;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    private void btnVolverActionPerformed(ActionEvent evt) {
        this.dispose();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OlvidarContraseña.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OlvidarContraseña.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OlvidarContraseña.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OlvidarContraseña.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OlvidarContraseña().setVisible(true);
            }
        });
    }
}
