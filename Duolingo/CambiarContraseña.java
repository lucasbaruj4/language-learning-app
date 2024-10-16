package Duolingo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CambiarContraseña extends JFrame {

    private int idEstudiante;
    private JTextField txtNuevaContrasena, txtValidarContrasena;
    private JButton btnCambiarContrasena, btnVolver;

    public CambiarContraseña(int idEstudiante) {
        this.idEstudiante = idEstudiante;
        initComponents();
    }

    private void initComponents() {
        setTitle("Cambiar Contraseña");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        JLabel lblTitulo = new JLabel("Cambiar Contraseña", JLabel.CENTER);
        JLabel lblNuevaContrasena = new JLabel("Ingresar nueva contraseña:");
        JLabel lblValidarContrasena = new JLabel("Validar nueva contraseña:");

        txtNuevaContrasena = new JTextField();
        txtValidarContrasena = new JTextField();

        btnCambiarContrasena = new JButton("Cambiar contraseña");
        btnVolver = new JButton("Volver");

        add(lblTitulo);
        add(new JLabel()); // empty cell
        add(lblNuevaContrasena);
        add(txtNuevaContrasena);
        add(lblValidarContrasena);
        add(txtValidarContrasena);
        add(btnCambiarContrasena);
        add(btnVolver);

        btnCambiarContrasena.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnCambiarContrasenaActionPerformed(evt);
            }
        });

        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });
    }

    private void btnCambiarContrasenaActionPerformed(ActionEvent evt) {
        String nuevaContrasena = txtNuevaContrasena.getText();
        String validarContrasena = txtValidarContrasena.getText();

        // Validaciones
        if (nuevaContrasena.isEmpty() || validarContrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ningún campo puede estar en blanco.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nuevaContrasena.length() < 4) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 4 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!nuevaContrasena.equals(validarContrasena)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Agregar impresión para verificar los valores antes de la actualización
        System.out.println("ID Estudiante: " + idEstudiante);
        System.out.println("Nueva Contraseña: " + nuevaContrasena);

        // Cambiar contraseña en la base de datos
        cambiarContrasena(nuevaContrasena);
    }

    private void cambiarContrasena(String nuevaContrasena) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Leocas", "postgres", "postgres");
            String query = "UPDATE Estudiante SET contrasena = ? WHERE id_estudiante = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, nuevaContrasena);
            pstmt.setInt(2, idEstudiante);
            int rowsUpdated = pstmt.executeUpdate();

            // Agregar impresión para verificar el resultado de la actualización
            System.out.println("Filas actualizadas: " + rowsUpdated);

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Contraseña modificada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el estudiante con el ID proporcionado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            this.dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cambiar la contraseña: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        this.dispose();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
                new CambiarContraseña(idEstudiante).setVisible(true);
            }
        });
    }
}
