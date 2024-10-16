package Duolingo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Traductor extends JFrame {

    public Traductor() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Traductor");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crear paneles para dividir la interfaz
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        leftPanel.setBackground(Color.PINK);
        rightPanel.setBackground(Color.ORANGE);
        leftPanel.setLayout(new GridBagLayout());
        rightPanel.setLayout(new GridBagLayout());

        // Configuración de componentes
        jLabel1 = new JLabel("Traductor", SwingConstants.CENTER);
        jLabel1.setFont(new Font("SansSerif", Font.BOLD, 18));
        jLabel2 = new JLabel("Idioma a traducir:");
        jComboBox1 = new JComboBox<>(new String[]{"Aleman", "Ingles", "Italiano"});
        jTextArea1 = new JTextArea(6, 20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setWrapStyleWord(true);
        jTextArea2 = new JTextArea(6, 20);
        jTextArea2.setLineWrap(true);
        jTextArea2.setWrapStyleWord(true);
        jTextArea2.setEditable(false);
        jButton1 = new JButton("Traducir");
        jButton2 = new JButton("Finalizar");
        jLabel3 = new JLabel("Español", SwingConstants.CENTER);
        jLabel4 = new JLabel("Idioma elegido", SwingConstants.CENTER);
        
        // Configuración de acción de los botones
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                translateText();
            }
        });

        jButton2.addActionListener((e) -> {
            dispose();
        });

        // Añadir componentes al panel izquierdo
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        leftPanel.add(jLabel3, gbc);
        gbc.gridy = 1;
        leftPanel.add(new JScrollPane(jTextArea1), gbc);

        // Añadir componentes al panel derecho
        gbc.gridy = 0;
        rightPanel.add(jLabel4, gbc);
        gbc.gridy = 1;
        rightPanel.add(new JScrollPane(jTextArea2), gbc);

        // Añadir botones al panel central
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.PINK);
        gbc.gridy = 0;
        centerPanel.add(jButton1, gbc);
        gbc.gridy = 1;
        centerPanel.add(jButton2, gbc);

        // Añadir todo al frame
        add(jLabel1, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
    }

    private void translateText() {
        String textToTranslate = jTextArea1.getText();
        String targetLanguage = "";

        switch (jComboBox1.getSelectedItem().toString()) {
            case "Aleman":
                targetLanguage = "de";
                break;
            case "Ingles":
                targetLanguage = "en";
                break;
            case "Italiano":
                targetLanguage = "it";
                break;
        }

        String translated = translateText(textToTranslate, "es", targetLanguage);
        jTextArea2.setText(translated);
    }

    private static String translateText(String text, String fromLang, String toLang) {
        try {
            String urlStr = String.format("https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s",
                    fromLang, toLang, text.replace(" ", "%20"));

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlStr))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonResponse = response.body();

            // Procesar la respuesta JSON
            String translatedText = jsonResponse.split("\"")[1];

            return translatedText;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error en la traducción";
        }
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Traductor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Traductor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Traductor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Traductor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Traductor().setVisible(true));
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
}
