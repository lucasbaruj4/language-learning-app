package Duolingo;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrarFrases extends javax.swing.JFrame {

    private javax.swing.JButton btnTerminar;
    private javax.swing.JButton btnRegistrarFrase;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblIngreseFrase;
    private javax.swing.JLabel lblIngreseTraduccion;
    private javax.swing.JTextField txtFrase;
    private javax.swing.JTextField txtTraduccion;

    public RegistrarFrases() {
        initComponents();
    }

    private void initComponents() {
        // Establecer la fuente estándar
        Font standardFont = new Font("SansSerif", Font.PLAIN, 12);
        Font boldFont = new Font("SansSerif", Font.BOLD, 12);
        Font titleFont = new Font("SansSerif", Font.BOLD, 18);

        lblTitulo = new JLabel("Registrar frases");
        lblTitulo.setFont(titleFont);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        lblIngreseFrase = new JLabel("Ingrese una frase:");
        lblIngreseFrase.setFont(boldFont);

        lblIngreseTraduccion = new JLabel("Ingrese su traducción:");
        lblIngreseTraduccion.setFont(boldFont);

        txtFrase = new JTextField(30); // Aumentar el ancho del JTextField
        txtFrase.setFont(standardFont);

        txtTraduccion = new JTextField(30); // Aumentar el ancho del JTextField
        txtTraduccion.setFont(standardFont);

        btnRegistrarFrase = new JButton("Registrar Frase");
        btnRegistrarFrase.setFont(standardFont);
        btnRegistrarFrase.setBackground(new Color(173, 216, 230));
        btnRegistrarFrase.addActionListener(this::btnRegistrarFraseActionPerformed);

        btnTerminar = new JButton("Terminar");
        btnTerminar.setFont(standardFont);
        btnTerminar.setBackground(new Color(173, 216, 230));
        btnTerminar.addActionListener(evt -> dispose());

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Registrar Frases");
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.ORANGE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        // Frase
        gbc.gridwidth = 1;
        gbc.gridy++;
        lblIngreseFrase.setHorizontalAlignment(SwingConstants.RIGHT);
        add(lblIngreseFrase, gbc);
        gbc.gridx = 1;
        add(txtFrase, gbc);

        // Traducción
        gbc.gridx = 0;
        gbc.gridy++;
        lblIngreseTraduccion.setHorizontalAlignment(SwingConstants.RIGHT);
        add(lblIngreseTraduccion, gbc);
        gbc.gridx = 1;
        add(txtTraduccion, gbc);

        // Botones
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        add(btnRegistrarFrase, gbc);
        gbc.gridx = 1;
        add(btnTerminar, gbc);

        pack();
        setLocationRelativeTo(null);

        // Añadir DocumentListener para validar los campos de texto
        txtFrase.getDocument().addDocumentListener(new TextFieldValidator(txtFrase));
        txtTraduccion.getDocument().addDocumentListener(new TextFieldValidator(txtTraduccion));
    }

    private void validateInput(JTextField textField) {
        String text = textField.getText();
        if (!text.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese solo caracteres válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            textField.setText(text.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]", ""));
        }
    }

    private void btnRegistrarFraseActionPerformed(ActionEvent evt) {
        String frase = txtFrase.getText();
        String traduccion = txtTraduccion.getText();
        int idIdioma = 1; // Cambiar según sea necesario
        int idTipoFrase = 1; // Personalizada
        int idEstado = 2; // No aprendido

        // Validar que los campos no estén vacíos
        if (frase.isEmpty() || traduccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ambos campos deben ser rellenados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Registrar la frase en la base de datos
        registrarFrase(idIdioma, frase, traduccion, idTipoFrase);

        // Asociar la frase al estudiante en la tabla estudiante_frase
        int idFrase = obtenerUltimoIdFrase();
        int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
        asociarFraseEstudiante(idEstudiante, idFrase, idEstado);
    }

    private void registrarFrase(int idIdioma, String frase, String traduccion, int idTipoFrase) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            String query = "INSERT INTO frase (id_idioma, frase, traduccion, id_tipo_frase) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, idIdioma);
            pstmt.setString(2, frase);
            pstmt.setString(3, traduccion);
            pstmt.setInt(4, idTipoFrase); // Personalizada
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Frase registrada con éxito.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al registrar la frase: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    private int obtenerUltimoIdFrase() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int idFrase = -1;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT MAX(id_frase) AS id_frase FROM frase";
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                idFrase = rs.getInt("id_frase");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener el ID de la frase: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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
        return idFrase;
    }

    private void asociarFraseEstudiante(int idEstudiante, int idFrase, int idEstado) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            String query = "INSERT INTO estudiante_frase (id_estudiante, id_frase, id_estado) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, idEstudiante);
            pstmt.setInt(2, idFrase);
            pstmt.setInt(3, idEstado);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Frase asociada al estudiante con éxito.", "Asociación Exitosa", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al asociar la frase al estudiante: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        EventQueue.invokeLater(() -> new RegistrarFrases().setVisible(true));
    }

    // Clase interna para validar los campos de texto
    private class TextFieldValidator implements DocumentListener {
        private JTextField textField;

        public TextFieldValidator(JTextField textField) {
            this.textField = textField;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            validate();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validate();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            validate();
        }

        private void validate() {
            String text = textField.getText();
            if (!text.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese solo caracteres válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                textField.setText(text.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]", ""));
            }
        }
    }
}
