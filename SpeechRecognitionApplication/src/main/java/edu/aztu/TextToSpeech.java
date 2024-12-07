package edu.aztu;


import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import java.io.IOException;

import static edu.aztu.util.GmailFileSender.sendFileToGmail;

public class TextToSpeech {
    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration();

        config.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        config.setDictionaryPath("src\\main\\resources\\1970.dic");
        config.setLanguageModelPath("src\\main\\resources\\1970.lm");

        try {
            LiveSpeechRecognizer speech = new LiveSpeechRecognizer(config);
            speech.startRecognition(true);

            SpeechResult speechResult = null;

            while ((speechResult = speech.getResult()) != null) {
                String voiceCommand = speechResult.getHypothesis();
                System.out.println("Voice Command is " + voiceCommand);

                if (voiceCommand.equalsIgnoreCase("CHROME AÇ")) {
                    Runtime.getRuntime().exec("cmd.exe /c start chrome www.youtube.com");
                } else if (voiceCommand.equalsIgnoreCase("CHROME BAĞLA")) {
                    Runtime.getRuntime().exec("cmd.exe /c TASKKILL /IM chrome.exe");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
//        sendFileToGmail("C:\\Users\\99470\\Desktop\\Magistr\\magistr 3 (2023).pdf");
    }
}

