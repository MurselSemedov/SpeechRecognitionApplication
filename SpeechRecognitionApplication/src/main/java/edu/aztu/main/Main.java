package edu.aztu.main;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    private LiveSpeechRecognizer recognizer;
    private boolean isListening = false; // Dinləmə vəziyyəti

    public Main() {
        try {
            // Nitq tanıma üçün konfiqurasiya
            Configuration configuration = new Configuration();
            configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
            configuration.setDictionaryPath("src\\main\\resources\\1970.dic");
            configuration.setLanguageModelPath("src\\main\\resources\\1970.lm");

            recognizer = new LiveSpeechRecognizer(configuration);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Nitq tanıma sistemi yüklənmədi: " + e.getMessage(),
                    "Xəta", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void start() {
        // Swing interfeysi yaradılır
        JFrame frame = new JFrame("Nitq Tanıma Proqramı");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel statusLabel = new JLabel("Mikrofon düyməsinə basın və danışın.", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton micButton = new JButton("🎤 Mikrofon");
        micButton.setFont(new Font("Arial", Font.BOLD, 20));
        micButton.setBackground(new Color(76, 175, 80));
        micButton.setForeground(Color.WHITE);

        micButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                statusLabel.setText("Dinləmə başladı...");
                startListening();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                statusLabel.setText("Dinləmə dayandırıldı.");
                stopListening(statusLabel);
            }
        });

        frame.add(statusLabel, BorderLayout.CENTER);
        frame.add(micButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void startListening() {
        try {
            if (!isListening) {
                isListening = true;
                recognizer.startRecognition(true); // Nitq tanımanı işə salırıq
                System.out.println("Dinləmə başladı...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopListening(JLabel statusLabel) {
        if (isListening) {
            isListening = false;// Nitq tanımanı dayandırırıq
            System.out.println("Dinləmə dayandırıldı...");

            // Nitq nəticəsini alırıq
            String result = recognizer.getResult().getHypothesis();
            if (result != null && !result.isEmpty()) {
                statusLabel.setText("Aldığınız nitq: " + result);
                System.out.println("Aldığınız nitq: " + result);
            } else {
                statusLabel.setText("Heç bir nitq aşkar edilmədi.");
                System.out.println("Heç bir nitq aşkar edilmədi.");
            }
            recognizer.stopRecognition();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.start();
        });
    }
}

