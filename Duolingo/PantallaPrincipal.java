package Duolingo;

import javax.swing.*;
import java.awt.*;

public class PantallaPrincipal extends javax.swing.JFrame {

    // Variables declaration
    private JButton btnRegistrarPalabra;
    private JButton btnRegistrarFrase;
    private JButton btnTraductor;
    private JButton btnConfiguracion;
    private JButton btnCambiarIdioma;
    private JButton btnVerPalabrasRegistradas;
    private JButton btnVerOracionesPracticadas;
    private JButton btnVerFrasesPersonalizadas;
    private JButton btnVerFrasesComunes;
    private JButton btnVerPalabrasAprendidas;
    private JButton btnPracticarPalabras;
    private JButton btnPracticarPalabrasDificiles;
    private JButton btnPracticarFrasesComunes;
    private JButton btnPracticarPronunciaciones;
    private JButton btnPracticarOraciones;
    private JButton btnCerrarSesion;
    private JButton btnHacerPrueba;
    private JButton btnVerPruebas;
    private JLabel lblBienvenida;
    private JLabel lblAcciones;
    private JLabel lblPracticas;
    private JLabel lblVista;
    private JLabel lblTraduccion;
    private JPanel mainPanel;

    public PantallaPrincipal() {
        initComponents();
    }

    private void initComponents() {
        // Establecer la fuente estándar
        Font standardFont = new Font("SansSerif", Font.PLAIN, 12);
        Font boldFont = new Font("SansSerif", Font.BOLD, 12);
        Font titleFont = new Font("SansSerif", Font.BOLD, 18);

        SesionEstudiante sesionEstudiante = SesionEstudiante.getInstance();
        String nombreUsuario = sesionEstudiante.getNombreUsuario();

        lblBienvenida = new JLabel("Bienvenido " + nombreUsuario + ".");
        lblBienvenida.setFont(titleFont);
        lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);

        lblAcciones = new JLabel("¿Qué vamos a hacer hoy?");
        lblAcciones.setFont(boldFont);
        lblAcciones.setHorizontalAlignment(SwingConstants.CENTER);

        lblPracticas = new JLabel("Prácticas");
        lblPracticas.setFont(boldFont);
        lblPracticas.setHorizontalAlignment(SwingConstants.CENTER);

        lblVista = new JLabel("Vista");
        lblVista.setFont(boldFont);
        lblVista.setHorizontalAlignment(SwingConstants.CENTER);

        lblTraduccion = new JLabel("¿Necesitas traductor?");
        lblTraduccion.setFont(boldFont);
        lblTraduccion.setHorizontalAlignment(SwingConstants.CENTER);

        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.ORANGE); // Esta línea establece el color de fondo del mainPanel
        getContentPane().setBackground(Color.ORANGE); // Esta línea establece el color de fondo del contenido de la ventana

        // Agregar componentes al mainPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblBienvenida, gbc);

        gbc.gridy++;
        mainPanel.add(lblAcciones, gbc);

        // Botones de Registrar
        gbc.gridwidth = 1;
        gbc.gridy++;
        btnRegistrarPalabra = new JButton("Registrar nuevas palabras");
        btnRegistrarPalabra.setFont(standardFont);
        btnRegistrarPalabra.setBackground(new Color(173, 216, 230));
        btnRegistrarPalabra.addActionListener(evt -> btnRegistrarPalabraActionPerformed(evt));
        mainPanel.add(btnRegistrarPalabra, gbc);

        gbc.gridx++;
        btnRegistrarFrase = new JButton("Registrar frases");
        btnRegistrarFrase.setFont(standardFont);
        btnRegistrarFrase.setBackground(new Color(173, 216, 230));
        btnRegistrarFrase.addActionListener(evt -> btnRegistrarFraseActionPerformed(evt));
        mainPanel.add(btnRegistrarFrase, gbc);

        // Botones de Vista
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        mainPanel.add(lblVista, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        btnVerPalabrasRegistradas = new JButton("Ver palabras registradas");
        btnVerPalabrasRegistradas.setFont(standardFont);
        btnVerPalabrasRegistradas.setBackground(new Color(173, 216, 230));
        btnVerPalabrasRegistradas.addActionListener(evt -> btnVerPalabrasRegistradasActionPerformed(evt));
        mainPanel.add(btnVerPalabrasRegistradas, gbc);

        gbc.gridx++;
        btnVerOracionesPracticadas = new JButton("Ver oraciones practicadas");
        btnVerOracionesPracticadas.setFont(standardFont);
        btnVerOracionesPracticadas.setBackground(new Color(173, 216, 230));
        btnVerOracionesPracticadas.addActionListener(evt -> btnVerOracionesPracticadasActionPerformed(evt));
        mainPanel.add(btnVerOracionesPracticadas, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        btnVerFrasesPersonalizadas = new JButton("Ver frases personalizadas");
        btnVerFrasesPersonalizadas.setFont(standardFont);
        btnVerFrasesPersonalizadas.setBackground(new Color(173, 216, 230));
        btnVerFrasesPersonalizadas.addActionListener(evt -> btnVerFrasesPersonalizadasActionPerformed(evt));
        mainPanel.add(btnVerFrasesPersonalizadas, gbc);

        gbc.gridx++;
        btnVerFrasesComunes = new JButton("Ver frases comunes aprendidas");
        btnVerFrasesComunes.setFont(standardFont);
        btnVerFrasesComunes.setBackground(new Color(173, 216, 230));
        btnVerFrasesComunes.addActionListener(evt -> btnVerFrasesComunesActionPerformed(evt));
        mainPanel.add(btnVerFrasesComunes, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        btnVerPalabrasAprendidas = new JButton("Ver palabras aprendidas");
        btnVerPalabrasAprendidas.setFont(standardFont);
        btnVerPalabrasAprendidas.setBackground(new Color(173, 216, 230));
        btnVerPalabrasAprendidas.addActionListener(evt -> btnVerPalabrasAprendidasActionPerformed(evt));
        mainPanel.add(btnVerPalabrasAprendidas, gbc);

        // Botones de Prácticas
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        mainPanel.add(lblPracticas, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        btnPracticarPalabras = new JButton("Practicar palabras registradas");
        btnPracticarPalabras.setFont(standardFont);
        btnPracticarPalabras.setBackground(new Color(173, 216, 230));
        btnPracticarPalabras.addActionListener(evt -> btnPracticarPalabrasActionPerformed(evt));
        mainPanel.add(btnPracticarPalabras, gbc);

        gbc.gridx++;
        btnPracticarPalabrasDificiles = new JButton("Practicar palabras difíciles");
        btnPracticarPalabrasDificiles.setFont(standardFont);
        btnPracticarPalabrasDificiles.setBackground(new Color(173, 216, 230));
        btnPracticarPalabrasDificiles.addActionListener(evt -> btnPracticarPalabrasDificilesActionPerformed(evt));
        mainPanel.add(btnPracticarPalabrasDificiles, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        btnPracticarFrasesComunes = new JButton("Practicar frases comunes");
        btnPracticarFrasesComunes.setFont(standardFont);
        btnPracticarFrasesComunes.setBackground(new Color(173, 216, 230));
        btnPracticarFrasesComunes.addActionListener(evt -> btnPracticarFrasesComunesActionPerformed(evt));
        mainPanel.add(btnPracticarFrasesComunes, gbc);

        gbc.gridx++;
        btnPracticarPronunciaciones = new JButton("Practicar pronunciaciones");
        btnPracticarPronunciaciones.setFont(standardFont);
        btnPracticarPronunciaciones.setBackground(new Color(173, 216, 230));
        btnPracticarPronunciaciones.addActionListener(evt -> btnPracticarPronunciacionesActionPerformed(evt));
        mainPanel.add(btnPracticarPronunciaciones, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        btnPracticarOraciones = new JButton("Practicar oraciones");
        btnPracticarOraciones.setFont(standardFont);
        btnPracticarOraciones.setBackground(new Color(173, 216, 230));
        btnPracticarOraciones.addActionListener(evt -> btnPracticarOracionesActionPerformed(evt));
        mainPanel.add(btnPracticarOraciones, gbc);

        // Botones de Traducción y Configuración
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        mainPanel.add(lblTraduccion, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        btnTraductor = new JButton("Traductor");
        btnTraductor.setFont(standardFont);
        btnTraductor.setBackground(new Color(173, 216, 230));
        btnTraductor.addActionListener((e) -> {
            Traductor iniciarTraductor = new Traductor();
            iniciarTraductor.setVisible(true);
        });
        mainPanel.add(btnTraductor, gbc);

        gbc.gridx++;
        btnConfiguracion = new JButton("Ajustes");
        btnConfiguracion.setFont(standardFont);
        btnConfiguracion.setBackground(new Color(173, 216, 230));
        btnConfiguracion.addActionListener(evt -> btnConfiguracion(evt));
        mainPanel.add(btnConfiguracion, gbc);

        // Botones de Cerrar Sesión y Pruebas
        gbc.gridx = 0;
        gbc.gridy++;
        btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.setFont(standardFont);
        btnCerrarSesion.setBackground(new Color(173, 216, 230));
        btnCerrarSesion.addActionListener(evt -> btnCerrarSesionActionPerformed(evt));
        mainPanel.add(btnCerrarSesion, gbc);

        gbc.gridx++;
        btnHacerPrueba = new JButton("¡Hacer una prueba!");
        btnHacerPrueba.setFont(standardFont);
        btnHacerPrueba.setBackground(new Color(173, 216, 230));
        btnHacerPrueba.addActionListener(evt -> btnHacerPruebaActionPerformed(evt));
        mainPanel.add(btnHacerPrueba, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        btnVerPruebas = new JButton("Ver pruebas realizadas");
        btnVerPruebas.setFont(standardFont);
        btnVerPruebas.setBackground(new Color(173, 216, 230));
        btnVerPruebas.addActionListener(evt -> btnVerPruebasActionPerformed(evt));
        mainPanel.add(btnVerPruebas, gbc);

        add(mainPanel);

        pack();
        setLocationRelativeTo(null);
    }

    private void btnRegistrarPalabraActionPerformed(java.awt.event.ActionEvent evt) {
        RegistrarPalabras registrarPalabra = new RegistrarPalabras();
        registrarPalabra.setVisible(true);
    }

    private void btnRegistrarFraseActionPerformed(java.awt.event.ActionEvent evt) {
        RegistrarFrases registrarFrase = new RegistrarFrases();
        registrarFrase.setVisible(true);
    }

    private void btnVerPalabrasRegistradasActionPerformed(java.awt.event.ActionEvent evt) {
        SesionEstudiante sesionEstudiante = SesionEstudiante.getInstance();
        int idEstudiante = sesionEstudiante.getIdEstudiante();
        VerPalabrasRegistradas verPalabrasRegistradas = new VerPalabrasRegistradas(idEstudiante);
        verPalabrasRegistradas.setVisible(true);
    }

    private void btnVerOracionesPracticadasActionPerformed(java.awt.event.ActionEvent evt) {
        SesionEstudiante sesionEstudiante = SesionEstudiante.getInstance();
        int idEstudiante = sesionEstudiante.getIdEstudiante();
        VerOraciones verOraciones = new VerOraciones(idEstudiante);
        verOraciones.setVisible(true);
    }

    private void btnVerFrasesPersonalizadasActionPerformed(java.awt.event.ActionEvent evt) {
        int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
        VerFrasesPersonalizada verFrasesPersonalizada = new VerFrasesPersonalizada(idEstudiante);
        verFrasesPersonalizada.setVisible(true);
    }

    private void btnVerFrasesComunesActionPerformed(java.awt.event.ActionEvent evt) {
        int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
        VerFrasesComunes frasesComunes = new VerFrasesComunes(idEstudiante);
        frasesComunes.setVisible(true);
    }

    private void btnVerPalabrasAprendidasActionPerformed(java.awt.event.ActionEvent evt) {
        int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
        VerPalabrasAprendidas verPalabrasAprendidas = new VerPalabrasAprendidas(idEstudiante);
        verPalabrasAprendidas.setVisible(true);
    }

    private void btnPracticarPalabrasActionPerformed(java.awt.event.ActionEvent evt) {
        int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
        PracticarPalabrasRegistradas practicarPalabras = new PracticarPalabrasRegistradas(idEstudiante);
        practicarPalabras.setVisible(true);
    }

    private void btnPracticarPalabrasDificilesActionPerformed(java.awt.event.ActionEvent evt) {
        int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
        PracticarPalabrasDificiles practicarPalabrasDificiles = new PracticarPalabrasDificiles(idEstudiante);
        practicarPalabrasDificiles.setVisible(true);
    }

    private void btnConfiguracion(java.awt.event.ActionEvent evt) {
        int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
        Configuracion configuracion = new Configuracion(idEstudiante);
        configuracion.setVisible(true);
    }

    private void btnPracticarFrasesComunesActionPerformed(java.awt.event.ActionEvent evt) {
        int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
        PracticarFrasesComunes frasesComunes = new PracticarFrasesComunes(idEstudiante);
        frasesComunes.setVisible(true);
    }

    private void btnPracticarPronunciacionesActionPerformed(java.awt.event.ActionEvent evt) {
        int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
        PracticarPronunciaciones practicarPronunciaciones = new PracticarPronunciaciones(idEstudiante);
        practicarPronunciaciones.setVisible(true);
    }

    private void btnPracticarOracionesActionPerformed(java.awt.event.ActionEvent evt) {
        int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
        PracticarOraciones practicarOraciones = new PracticarOraciones(idEstudiante);
        practicarOraciones.setVisible(true);
    }

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro que deseas cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            PantallaInicial pantallaInicial = new PantallaInicial();
            pantallaInicial.setVisible(true);
            this.dispose();
        }
    }

    private void btnHacerPruebaActionPerformed(java.awt.event.ActionEvent evt) {
        int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
        Prueba prueba = new Prueba(idEstudiante);
        prueba.setVisible(true);
    }

    private void btnVerPruebasActionPerformed(java.awt.event.ActionEvent evt) {
        int idEstudiante = SesionEstudiante.getInstance().getIdEstudiante();
        VerPruebas verPruebas = new VerPruebas(idEstudiante);
        verPruebas.setVisible(true);
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
                new PantallaPrincipal().setVisible(true);
            }
        });
    }
}
