package edu.aztu;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GmailTest {

    public static void main(String[] args) throws IOException {
        Configuration config1 = new Configuration();
        config1.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        config1.setDictionaryPath("src\\main\\resources\\1970.dic");
        config1.setLanguageModelPath("src\\main\\resources\\1970.lm");
        LiveSpeechRecognizer speech = new LiveSpeechRecognizer(config1);
        speech.startRecognition(true);

        SpeechResult speechResult;

        while ((speechResult = speech.getResult()) != null) {
            String voiceCommand = speechResult.getHypothesis();
            voiceCommand = voiceCommand.replace(" at ", "@").replace(" dot ", ".");
            Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
            Matcher matcher = emailPattern.matcher(voiceCommand);

            System.out.println("Voice Command is " + voiceCommand);

        }


    }

}
