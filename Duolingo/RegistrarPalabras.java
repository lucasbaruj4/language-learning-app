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

public class RegistrarPalabras extends JFrame {

    private JButton btnRegistrar;
    private JButton btnVolver;
    private JComboBox<String> cmbTipoPalabra;
    private JComboBox<String> cmbDificultad;
    private JComboBox<String> cmbIdioma;
    private JLabel lblTitulo;
    private JLabel lblPalabra;
    private JLabel lblTraduccion;
    private JLabel lblSignificado;
    private JLabel lblTipoPalabra;
    private JLabel lblDificultad;
    private JLabel lblIdioma;
    private JTextField txtPalabra;
    private JTextField txtTraduccion;
    private JTextField txtSignificado;

    public RegistrarPalabras() {
        initComponents();
    }

    private void initComponents() {
        // Establecer la fuente estándar
        Font standardFont = new Font("SansSerif", Font.PLAIN, 12);
        Font boldFont = new Font("SansSerif", Font.BOLD, 12);
        Font titleFont = new Font("SansSerif", Font.BOLD, 18);

        lblTitulo = new JLabel("Registrar nuevas palabras");
        lblTitulo.setFont(titleFont);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        lblPalabra = new JLabel("Ingrese la nueva palabra:");
        lblPalabra.setFont(boldFont);
        
        lblTraduccion = new JLabel("Ingrese su traducción:");
        lblTraduccion.setFont(boldFont);

        lblSignificado = new JLabel("Ingrese su significado:");
        lblSignificado.setFont(boldFont);

        lblTipoPalabra = new JLabel("Tipo de palabra:");
        lblTipoPalabra.setFont(boldFont);

        lblDificultad = new JLabel("Dificultad:");
        lblDificultad.setFont(boldFont);

        lblIdioma = new JLabel("Idioma:");
        lblIdioma.setFont(boldFont);

        txtPalabra = new JTextField(20);
        txtPalabra.setFont(standardFont);

        txtTraduccion = new JTextField(20);
        txtTraduccion.setFont(standardFont);

        txtSignificado = new JTextField(20);
        txtSignificado.setFont(standardFont);

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setFont(standardFont);
        btnRegistrar.setBackground(new Color(173, 216, 230));
        btnRegistrar.addActionListener(this::btnRegistrarActionPerformed);

        btnVolver = new JButton("Volver");
        btnVolver.setFont(standardFont);
        btnVolver.setBackground(new Color(173, 216, 230));
        btnVolver.addActionListener(this::btnVolverActionPerformed);

        cmbTipoPalabra = new JComboBox<>(new String[]{"Sustantivo", "Verbo", "Adjetivo", "Preposición", "Artículo", "Pronombre"});
        cmbTipoPalabra.setFont(standardFont);

        cmbDificultad = new JComboBox<>(new String[]{"Intermedio", "Difícil"});
        cmbDificultad.setFont(standardFont);

        cmbIdioma = new JComboBox<>(new String[]{"Alemán", "Inglés", "Italiano"});
        cmbIdioma.setFont(standardFont);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Registrar Palabras");
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

        // Palabra
        gbc.gridwidth = 1;
        gbc.gridy++;
        lblPalabra.setHorizontalAlignment(SwingConstants.RIGHT);
        add(lblPalabra, gbc);
        gbc.gridx = 1;
        add(txtPalabra, gbc);

        // Traducción
        gbc.gridx = 0;
        gbc.gridy++;
        lblTraduccion.setHorizontalAlignment(SwingConstants.RIGHT);
        add(lblTraduccion, gbc);
        gbc.gridx = 1;
        add(txtTraduccion, gbc);

        // Significado
        gbc.gridx = 0;
        gbc.gridy++;
        lblSignificado.setHorizontalAlignment(SwingConstants.RIGHT);
        add(lblSignificado, gbc);
        gbc.gridx = 1;
        add(txtSignificado, gbc);

        // Tipo de palabra
        gbc.gridx = 0;
        gbc.gridy++;
        lblTipoPalabra.setHorizontalAlignment(SwingConstants.RIGHT);
        add(lblTipoPalabra, gbc);
        gbc.gridx = 1;
        add(cmbTipoPalabra, gbc);

        // Dificultad
        gbc.gridx = 0;
        gbc.gridy++;
        lblDificultad.setHorizontalAlignment(SwingConstants.RIGHT);
        add(lblDificultad, gbc);
        gbc.gridx = 1;
        add(cmbDificultad, gbc);

        // Idioma
        gbc.gridx = 0;
        gbc.gridy++;
        lblIdioma.setHorizontalAlignment(SwingConstants.RIGHT);
        add(lblIdioma, gbc);
        gbc.gridx = 1;
        add(cmbIdioma, gbc);

        // Botones
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        add(btnRegistrar, gbc);
        gbc.gridx = 1;
        add(btnVolver, gbc);

        pack();
        setLocationRelativeTo(null);

        // Añadir DocumentListener para validar los campos de texto
        txtPalabra.getDocument().addDocumentListener(new TextFieldValidator(txtPalabra));
        txtTraduccion.getDocument().addDocumentListener(new TextFieldValidator(txtTraduccion));
        txtSignificado.getDocument().addDocumentListener(new TextFieldValidator(txtSignificado));
    }

    private void btnRegistrarActionPerformed(ActionEvent evt) {
        String palabra = txtPalabra.getText();
        String traduccion = txtTraduccion.getText();
        String significado = txtSignificado.getText();

        int tipoPalabra = cmbTipoPalabra.getSelectedIndex() + 1;
        int dificultad;
        if (cmbDificultad.getSelectedItem().equals("Intermedio")) {
            dificultad = 2;
        } else {
            dificultad = 3;
        }
        int idioma = cmbIdioma.getSelectedIndex() + 1;

        if (palabra.isEmpty() || traduccion.isEmpty() || significado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas registrar esta palabra?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        registrarPalabra(SesionEstudiante.getInstance().getIdEstudiante(), idioma, palabra, traduccion, significado, dificultad, tipoPalabra);
    }

    private void registrarPalabra(int idEstudiante, int idIdioma, String palabra, String traduccion, String significado, int idDificultad, int idTipoPalabra) {
        Connection conn = null;
        PreparedStatement pstmtPalabra = null;
        PreparedStatement pstmtEstudiantePalabra = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            String queryPalabra = "INSERT INTO palabra (id_idioma, palabra, traduccion, significado, id_dificultad, id_tipo_palabra) VALUES (?, ?, ?, ?, ?, ?) RETURNING id_palabra";
            pstmtPalabra = conn.prepareStatement(queryPalabra);
            pstmtPalabra.setInt(1, idIdioma);
            pstmtPalabra.setString(2, palabra);
            pstmtPalabra.setString(3, traduccion);
            pstmtPalabra.setString(4, significado);
            pstmtPalabra.setInt(5, idDificultad);
            pstmtPalabra.setInt(6, idTipoPalabra);
            ResultSet rs = pstmtPalabra.executeQuery();

            if (rs.next()) {
                int idPalabra = rs.getInt("id_palabra");

                String queryEstudiantePalabra = "INSERT INTO estudiante_palabra (id_estudiante, id_palabra, id_estado) VALUES (?, ?, ?)";
                pstmtEstudiantePalabra = conn.prepareStatement(queryEstudiantePalabra);
                pstmtEstudiantePalabra.setInt(1, idEstudiante);
                pstmtEstudiantePalabra.setInt(2, idPalabra);
                pstmtEstudiantePalabra.setInt(3, 2); // 2 significa "no aprendido"
                pstmtEstudiantePalabra.executeUpdate();

                JOptionPane.showMessageDialog(this, "Palabra registrada con éxito.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al registrar la palabra: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pstmtPalabra != null) {
                    pstmtPalabra.close();
                }
                if (pstmtEstudiantePalabra != null) {
                    pstmtEstudiantePalabra.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
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

        EventQueue.invokeLater(() -> new RegistrarPalabras().setVisible(true));
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
