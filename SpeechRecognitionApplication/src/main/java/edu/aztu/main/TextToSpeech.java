package edu.aztu.main;
import edu.aztu.util.FileNumber;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import static edu.aztu.util.GmailFileSender.sendFileToGmail;
import static edu.aztu.util.GoogleTTS.sayText;

public class TextToSpeech {
    private static File currentDirectory = new File(System.getProperty("user.home"));

    public static void main(String[] args) throws Exception {
        Configuration config1 = new Configuration();
        config1.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        config1.setDictionaryPath("src\\main\\resources\\1970.dic");
        config1.setLanguageModelPath("src\\main\\resources\\1970.lm");

        LiveSpeechRecognizer speech = new LiveSpeechRecognizer(config1);
            speech.startRecognition(true);

            SpeechResult speechResult;

            while ((speechResult = speech.getResult()) != null) {
                String voiceCommand = speechResult.getHypothesis();
                System.out.println("Voice Command is " + voiceCommand);

                if(voiceCommand.contains("ACCESS THE")){
                    navigateToFolder(voiceCommand);
                } else if (voiceCommand.contains("GO BACK")) {
                    goBack();
                } else if (voiceCommand.contains("SELECT FILE")) {
                    String selectedFile = selectFile(voiceCommand);
                    if(!selectedFile.contains("not found")){
                        sayText("Gmail adresinizi söyleyin");
                        while((speechResult = speech.getResult()) != null) {
                            String gmailCommand = speechResult.getHypothesis();
                            System.out.println("Gmail Command is " + gmailCommand.split(" ")[0]);
                            String gmail = requireGmail(gmailCommand);
                            if (!gmail.contains("not found")) {
                                sendFileToGmail(selectedFile, gmail.toLowerCase());
                                break;
                            } else {
                                sayText("Gmail yanlisdir,yeniden soyleyin");
                            }
                        }
                    }
                }
            }
    }

    private static void navigateToFolder(String voiceCommand) throws IOException {
        String folderName = voiceCommand.replace("ACCESS THE", "").trim();
        if(!folderName.isBlank()){
        String folderEditName = folderName.charAt(0) + folderName.substring(1).toLowerCase(Locale.ROOT);
        File targetFolder = new File(currentDirectory, folderEditName);

        if (targetFolder.exists() && targetFolder.isDirectory()) {
            currentDirectory = targetFolder;
            Runtime.getRuntime().exec("cmd.exe /c start " + currentDirectory);
            System.out.println("Current Directory : " + currentDirectory.getAbsolutePath());
        } else {
            System.out.println("Folder not found : " + folderName);
        }}
    }

    private static void goBack() throws IOException {
        File parentDirectory = currentDirectory.getParentFile();
        if (parentDirectory != null) {
            currentDirectory = parentDirectory;
            Runtime.getRuntime().exec("cmd.exe /c start " + currentDirectory);
            System.out.println("Current Directory: " + currentDirectory.getAbsolutePath());
        } else {
            System.out.println("You are already at the highest level.");
        }
    }

    private static String selectFile(String voiceCommand) throws Exception {
        String fileName = voiceCommand.replace("SELECT FILE ", "").trim();
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

    private static String requireGmail(String voiceCommand) throws IOException {
            if(voiceCommand.contains("@GMAIL.COM")){
                return voiceCommand.split(" ")[0];
            }
        return "Gmail not found";
    }
}


//
//        else if (voiceCommand.contains("CLOSE")) {
//            String a = voiceCommand.split("CLOSE ")[1];
//            String b = a.charAt(0) + a.substring(1).toLowerCase(Locale.ROOT);;
//            Runtime.getRuntime().exec("cmd.exe /c powershell -Command \"Get-Process | Where-Object { $_.MainWindowTitle -eq '"
//                    + b + "' } | ForEach-Object { Stop-Process -Id $_.Id }\"");
//        }