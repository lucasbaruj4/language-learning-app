package Duolingo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class RegistrarUsuario extends JFrame {

    // Variables declaration - do not modify
    private JButton btnRegistrarse;
    private JButton btnVolver;
    private JCheckBox chkAleman;
    private JCheckBox chkIngles;
    private JCheckBox chkItaliano;
    private JComboBox<String> cmbIdiomaPredeterminado;
    private JLabel lblTitulo;
    private JLabel lblNombre;
    private JLabel lblApellido;
    private JLabel lblUsuario;
    private JLabel lblContrasena;
    private JLabel lblIdiomasAprender;
    private JLabel lblIdiomaPredeterminado;
    private JLabel lblPalabraSecreta;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtUsuario;
    private JTextField txtContrasena;
    private JTextField txtPalabraSecreta;

    public RegistrarUsuario() {
        initComponents();
    }

    private void initComponents() {
        // Establecer la fuente estándar
        Font standardFont = new Font("SansSerif", Font.PLAIN, 12);
        Font boldFont = new Font("SansSerif", Font.BOLD, 12);
        Font titleFont = new Font("SansSerif", Font.BOLD, 18);

        lblTitulo = new JLabel();
        lblNombre = new JLabel();
        lblApellido = new JLabel();
        lblUsuario = new JLabel();
        lblContrasena = new JLabel();
        lblIdiomasAprender = new JLabel();
        lblIdiomaPredeterminado = new JLabel();
        lblPalabraSecreta = new JLabel();
        chkAleman = new JCheckBox();
        chkIngles = new JCheckBox();
        chkItaliano = new JCheckBox();
        cmbIdiomaPredeterminado = new JComboBox<>();
        txtNombre = new JTextField(20); // Limitar el ancho de los JTextField
        txtApellido = new JTextField(20); // Limitar el ancho de los JTextField
        txtUsuario = new JTextField(20); // Limitar el ancho de los JTextField
        txtContrasena = new JTextField(20); // Limitar el ancho de los JTextField
        txtPalabraSecreta = new JTextField(20); // Limitar el ancho de los JTextField
        btnRegistrarse = new JButton();
        btnVolver = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.ORANGE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        lblTitulo.setText("Registrarse");
        lblTitulo.setFont(titleFont);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        // Nombre
        gbc.gridwidth = 1;
        gbc.gridy++;
        lblNombre.setText("Nombre:");
        lblNombre.setFont(boldFont);
        add(lblNombre, gbc);
        gbc.gridx = 1;
        add(txtNombre, gbc);

        // Apellido
        gbc.gridx = 0;
        gbc.gridy++;
        lblApellido.setText("Apellido:");
        lblApellido.setFont(boldFont);
        add(lblApellido, gbc);
        gbc.gridx = 1;
        add(txtApellido, gbc);

        // Usuario
        gbc.gridx = 0;
        gbc.gridy++;
        lblUsuario.setText("Usuario:");
        lblUsuario.setFont(boldFont);
        add(lblUsuario, gbc);
        gbc.gridx = 1;
        add(txtUsuario, gbc);

        // Contraseña
        gbc.gridx = 0;
        gbc.gridy++;
        lblContrasena.setText("Contraseña:");
        lblContrasena.setFont(boldFont);
        add(lblContrasena, gbc);
        gbc.gridx = 1;
        add(txtContrasena, gbc);

        // Palabra secreta
        gbc.gridx = 0;
        gbc.gridy++;
        lblPalabraSecreta.setText("Palabra secreta:");
        lblPalabraSecreta.setFont(boldFont);
        add(lblPalabraSecreta, gbc);
        gbc.gridx = 1;
        add(txtPalabraSecreta, gbc);

        // Idiomas a aprender
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        lblIdiomasAprender.setText("¿Qué idiomas deseas aprender?");
        lblIdiomasAprender.setFont(boldFont);
        lblIdiomasAprender.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblIdiomasAprender, gbc);

        // Checkbox Alemán
        gbc.gridwidth = 1;
        gbc.gridy++;
        chkAleman.setText("Alemán");
        chkAleman.setFont(standardFont);
        add(chkAleman, gbc);

        // Checkbox Inglés
        gbc.gridy++;
        chkIngles.setText("Inglés");
        chkIngles.setFont(standardFont);
        add(chkIngles, gbc);

        // Checkbox Italiano
        gbc.gridy++;
        chkItaliano.setText("Italiano");
        chkItaliano.setFont(standardFont);
        add(chkItaliano, gbc);

        // Idioma predeterminado
        gbc.gridx = 0;
        gbc.gridy++;
        lblIdiomaPredeterminado.setText("¿Cuál será tu idioma predeterminado?");
        lblIdiomaPredeterminado.setFont(boldFont);
        add(lblIdiomaPredeterminado, gbc);
        gbc.gridx = 1;
        cmbIdiomaPredeterminado.setModel(new DefaultComboBoxModel<>(new String[]{"Alemán", "Inglés", "Italiano"}));
        add(cmbIdiomaPredeterminado, gbc);

        // Botones
        gbc.gridx = 0;
        gbc.gridy++;
        btnRegistrarse.setText("Registrarse");
        btnRegistrarse.setFont(standardFont);
        btnRegistrarse.setBackground(new Color(173, 216, 230));
        btnRegistrarse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnRegistrarseActionPerformed(evt);
            }
        });
        add(btnRegistrarse, gbc);

        gbc.gridx = 1;
        btnVolver.setText("Volver");
        btnVolver.setFont(standardFont);
        btnVolver.setBackground(new Color(173, 216, 230));
        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });
        add(btnVolver, gbc);

        pack();
        setLocationRelativeTo(null);
    }

    private void btnRegistrarseActionPerformed(ActionEvent evt) {
        // Capturar los datos de los campos de texto
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String usuario = txtUsuario.getText();
        String contrasena = txtContrasena.getText();
        String palabraSecreta = txtPalabraSecreta.getText();

        // Validaciones
        if (nombre.length() < 3 || nombre.length() > 20) {
            JOptionPane.showMessageDialog(this, "El nombre debe tener entre 3 y 20 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (apellido.length() < 3 || apellido.length() > 20) {
            JOptionPane.showMessageDialog(this, "El apellido debe tener entre 3 y 20 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (usuario.length() < 3 || usuario.length() > 20) {
            JOptionPane.showMessageDialog(this, "El usuario debe tener entre 3 y 20 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (contrasena.length() < 4) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 4 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (palabraSecreta.length() < 3 || palabraSecreta.length() > 20) {
            JOptionPane.showMessageDialog(this, "La palabra secreta debe tener entre 3 y 20 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!chkAleman.isSelected() && !chkIngles.isSelected() && !chkItaliano.isSelected()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos un idioma para aprender.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Determinar el idioma elegido (id 1 = aleman, id 2 = ingles, id 3 = italiano)
        int idiomaElegido = cmbIdiomaPredeterminado.getSelectedIndex() + 1;

        // Fecha de creación del usuario
        LocalDate fechaCreacion = LocalDate.now();

        // Debug: imprimir datos capturados
        System.out.println("Datos capturados:");
        System.out.println("Nombre: " + nombre);
        System.out.println("Apellido: " + apellido);
        System.out.println("Usuario: " + usuario);
        System.out.println("Contraseña: " + contrasena);
        System.out.println("Palabra Secreta: " + palabraSecreta);
        System.out.println("Idioma Elegido (ID): " + idiomaElegido);
        System.out.println("Fecha de Creación: " + fechaCreacion);

        registrarUsuario(nombre, apellido, usuario, contrasena, palabraSecreta, idiomaElegido, fechaCreacion);
    }

    private void registrarUsuario(String nombre, String apellido, String usuario, String contrasena, String palabraSecreta, int idiomaElegido, LocalDate fechaCreacion) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            String query = "INSERT INTO estudiante (nombre, apellido, usuario, contrasena, palabra_secreta, id_idioma, fecha_creacion) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, usuario);
            pstmt.setString(4, contrasena);
            pstmt.setString(5, palabraSecreta);
            pstmt.setInt(6, idiomaElegido);
            pstmt.setDate(7, java.sql.Date.valueOf(fechaCreacion));
            pstmt.executeUpdate();
            System.out.println("Usuario registrado con éxito!");
        } catch (SQLException e) {
            e.printStackTrace();
            // Debug: imprimir excepción
            System.out.println("Error al registrar usuario: " + e.getMessage());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void btnVolverActionPerformed(ActionEvent evt) {
        dispose();
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
                new RegistrarUsuario().setVisible(true);
            }
        });
    }
}
