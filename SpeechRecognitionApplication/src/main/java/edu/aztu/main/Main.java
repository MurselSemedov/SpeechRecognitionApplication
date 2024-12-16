package edu.aztu.main;

import edu.aztu.util.FileNumber;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static edu.aztu.util.GmailFileSender.sendFileToGmail;
import static edu.aztu.util.GoogleTTS.sayText;

public class Main {
    private String command;
    private LiveSpeechRecognizer recognizer;
    private boolean isListening = false; // Dinləmə vəziyyəti
    private static File currentDirectory = new File(System.getProperty("user.home"));
    private boolean gmailTrue = false;
    private boolean sendingGmail = false;
    private String sendingFile;
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
        ImageIcon microphone = new ImageIcon("C:\\Workspace\\SpeechRecognitionApplication\\SpeechRecognitionApplication\\src\\main\\resources\\microphone.jpeg");
        int buttonWidth = 50; // İstədiyiniz düymə eni
        int buttonHeight = 50; // İstədiyiniz düymə hündürlüyü
        Image resizedImage = microphone.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        JButton micButton = new JButton(resizedIcon);
        micButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        micButton.setFont(new Font("Arial", Font.BOLD, 20));
        micButton.setBackground(new Color(76, 175, 80));
        micButton.setForeground(Color.WHITE);

        micButton.addMouseListener(new java.awt.event.MouseAdapter() {


            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if(sendingGmail){
                   statusLabel.setText(command);
                   sendingGmail = false;
                }else {
                    statusLabel.setText("Dinləmə başladı...");
                    startListening();
                }

            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                statusLabel.setText(command);
                stopListening(statusLabel);
            }
        });
        frame.setLocationRelativeTo(null);
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
                SpeechResult speechResult;
                if ((speechResult = recognizer.getResult()) != null) {
                    String voiceCommand = speechResult.getHypothesis();
                    command = voiceCommand;
                    if(voiceCommand.contains("OPEN") && !gmailTrue){
                        System.out.println("Voice Command is " + voiceCommand);
                        navigateToFolder(voiceCommand);
                    } else if (voiceCommand.contains("GO BACK") && !gmailTrue) {
                        System.out.println("Voice Command is " + voiceCommand);
                        goBack();
                    } else if (voiceCommand.contains("SEND FILE") || gmailTrue) {
                        System.out.println("Voice Command is " + voiceCommand);
                        if(!gmailTrue){
                             sendingFile = selectFile(voiceCommand);
                        }
                        if(!sendingFile.contains("not found") || gmailTrue){
                            if(gmailTrue) {
                                voiceCommand = voiceCommand.split(" ")[0];
                                command = voiceCommand;
                                System.out.println("Gmail Command is " + voiceCommand);
                                if (!voiceCommand.contains("not found")) {
                                    sendFileToGmail(sendingFile, voiceCommand.toLowerCase());
                                    command = "Gmail uğurla göndərildi";
                                    gmailTrue = false;
                                } else {
                                    sayText("Gmail yanlisdir,yeniden soyleyin");
                                }
                            }else{
                                sayText("Gmail adresinizi söyleyin");
                                gmailTrue = true;
                            }
                            }
                        }
                    }
                }
            }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopListening(JLabel statusLabel) {
        if (isListening) {
            isListening = false;// Nitq tanımanı dayandırırıq
            // Nitq nəticəsini alırıq
//            String result = recognizer.getResult().getHypothesis();
//            if (result != null && !result.isEmpty()) {
//                statusLabel.setText("Aldığınız nitq: " + result);
//                System.out.println("Aldığınız nitq: " + result);
//            } else {
//                statusLabel.setText("Heç bir nitq aşkar edilmədi.");
//                System.out.println("Heç bir nitq aşkar edilmədi.");
//            }
            recognizer.stopRecognition();
            System.out.println("Dinləmə dayandırıldı...");
        }
    }
    private static void navigateToFolder(String voiceCommand){
        String folderName = voiceCommand.replace("OPEN", "").trim();
        if(!folderName.isBlank()){
            String folderEditName = folderName.charAt(0) + folderName.substring(1).toLowerCase(Locale.ROOT);
            File targetFolder = new File(currentDirectory, folderEditName);

            if (targetFolder.exists() && targetFolder.isDirectory()) {
                currentDirectory = targetFolder;
                try {
                    Runtime.getRuntime().exec("cmd.exe /c start " + currentDirectory);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Current Directory : " + currentDirectory.getAbsolutePath());
            } else {
                System.out.println("Folder not found : " + folderName);
            }}
    }

    private static void goBack(){
        File parentDirectory = currentDirectory.getParentFile();
        if (parentDirectory != null) {
            currentDirectory = parentDirectory;
            try {
                Runtime.getRuntime().exec("cmd.exe /c start " + currentDirectory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Current Directory: " + currentDirectory.getAbsolutePath());
        } else {
            System.out.println("You are already at the highest level.");
        }
    }

    private static String selectFile(String voiceCommand){
        String fileName = voiceCommand.replace("SEND FILE ", "").trim();
        File[] files = currentDirectory.listFiles();
        assert files != null;
        if(FileNumber.getFileNumber().contains(fileName)){
            File targetFile = files[FileNumber.valueOf(fileName).getValue()-1].getAbsoluteFile();
            if (targetFile.exists() && targetFile.isFile()) {
                System.out.println("File Selected: " + targetFile.getAbsolutePath());
                return targetFile.getAbsolutePath();
            }}
        return "File not found : " + fileName;
    }

    private static String requireGmail(String voiceCommand){
        if(voiceCommand.contains("@GMAIL.COM")){
            return voiceCommand.split(" ")[0];
        }
        return "Gmail not found";
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.start();
        });
    }
}

