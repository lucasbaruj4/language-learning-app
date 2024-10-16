package Duolingo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PantallaInicial extends JFrame {

    // Variables declaration
    private JButton btnRegistrar;
    private JButton btnIniciarSesion;
    private JButton btnOlvidarContrasena;
    private JLabel lblBienvenido;
    private JLabel lblUsuario;
    private JLabel lblContrasena;
    private JPasswordField txtContrasena;
    private JSeparator separator;
    private JTextField txtUsuario;

    public PantallaInicial() {
        initComponents();
    }

    private void initComponents() {
        // Establecer la fuente estándar
        Font standardFont = new Font("SansSerif", Font.PLAIN, 12);
        Font boldFont = new Font("SansSerif", Font.BOLD, 12);
        Font titleFont = new Font("SansSerif", Font.BOLD, 18);

        lblBienvenido = new JLabel();
        lblUsuario = new JLabel();
        lblContrasena = new JLabel();
        txtUsuario = new JTextField(20); 
        separator = new JSeparator();
        txtContrasena = new JPasswordField(20); 
        btnIniciarSesion = new JButton();
        btnRegistrar = new JButton();
        btnOlvidarContrasena = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.ORANGE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        lblBienvenido.setText("Bienvenido a Leocas Learning");
        lblBienvenido.setFont(titleFont);
        lblBienvenido.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblBienvenido, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        lblUsuario.setText("Usuario");
        lblUsuario.setFont(boldFont);
        lblUsuario.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblUsuario, gbc);

        gbc.gridx++;
        add(txtUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        lblContrasena.setText("Contraseña");
        lblContrasena.setFont(boldFont);
        lblContrasena.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblContrasena, gbc);

        gbc.gridx++;
        add(txtContrasena, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        btnIniciarSesion.setText("Iniciar sesión");
        btnIniciarSesion.setFont(standardFont);
        btnIniciarSesion.setBackground(new Color(173, 216, 230));
        btnIniciarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnIniciarSesionActionPerformed(evt);
            }
        });
        add(btnIniciarSesion, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;

        btnRegistrar.setText("Registrarse");
        btnRegistrar.setFont(standardFont);
        btnRegistrar.setBackground(new Color(173, 216, 230));
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });
        add(btnRegistrar, gbc);

        gbc.gridx++;

        btnOlvidarContrasena.setText("Olvidé mi contraseña");
        btnOlvidarContrasena.setFont(standardFont);
        btnOlvidarContrasena.setBackground(new Color(173, 216, 230));
        btnOlvidarContrasena.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOlvidarContrasenaActionPerformed(evt);
            }
        });
        add(btnOlvidarContrasena, gbc);

        pack();
        setLocationRelativeTo(null);
    }

    private void btnIniciarSesionActionPerformed(ActionEvent evt) {
        String usuario = txtUsuario.getText();
        String contrasena = new String(txtContrasena.getPassword());

        // Validar que los campos no estén vacíos
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ambos campos deben ser rellenados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar credenciales
        try {
            if (verificarCredenciales(usuario, contrasena)) {
                // Ir a PantallaPrincipal
                PantallaPrincipal pantallaPrincipal = new PantallaPrincipal();
                pantallaPrincipal.setVisible(true);
                this.dispose(); // Cierra la pantalla actual
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean verificarCredenciales(String usuario, String contrasena) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT id_estudiante, contrasena, nombre FROM estudiante WHERE usuario = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, usuario);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("contrasena");
                if (storedPassword.equals(contrasena)) {
                    int idEstudiante = rs.getInt("id_estudiante");
                    String nombre = rs.getString("nombre");
                    SesionEstudiante.getInstance().iniciarSesion(idEstudiante, nombre);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Usuario inexistente.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
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

    private void btnRegistrarActionPerformed(ActionEvent evt) {
        RegistrarUsuario registrarUsuario = new RegistrarUsuario();
        registrarUsuario.setVisible(true);
    }

    private void btnOlvidarContrasenaActionPerformed(ActionEvent evt) {
        OlvidarContraseña olvidarContraseña = new OlvidarContraseña();
        olvidarContraseña.setVisible(true);
    }

    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PantallaInicial().setVisible(true);
            }
        });
    }
}
