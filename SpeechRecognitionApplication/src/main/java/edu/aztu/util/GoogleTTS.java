package edu.aztu.util;

import com.google.cloud.texttospeech.v1.*;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;

public class GoogleTTS{
    public static void main(String[] args) throws Exception {
//        sayText("sakinaquliyeva15@gmail.com");
//        sayText("mirzyevelgun439@gmail.com");
        sayText("Gmail adresinizi söyleyin");
//        sayText("amrahli112@gmail.com");
//        sayText("Hangi gmail adresine göndereceksiniz ");
    }



    public static void sayText(String text){
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            // Mətn və səsin dili
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("tr-TR")
//                    .setName("tr-TR-Wavenet-C")
                    .setSsmlGender(SsmlVoiceGender.FEMALE)
                    .build();
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.LINEAR16) // PCM formatında audio
                    .setSpeakingRate(1)
                    .build();
//            // Səs yaratmaq
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
////             Çıxış faylını yazmaq
            playAudio(response.getAudioContent().toByteArray());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static void playAudio(byte[] audioData) throws Exception {
        // Byte array-dən InputStream yaradın
        ByteArrayInputStream inputStream = new ByteArrayInputStream(audioData);

        // Audio formatını təyin edin
        AudioFormat format = new AudioFormat(24000, 16, 1, true, false);
        AudioInputStream audioStream = new AudioInputStream(inputStream, format, audioData.length / format.getFrameSize());

        // DataLine oynatmaq üçün istifadə edin
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();

        // Səsi oynat
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = audioStream.read(buffer)) != -1) {
            line.write(buffer, 0, bytesRead);
        }

        // Oynatma tamamlanır
        line.drain();
        line.close();
        audioStream.close();
    }
    }

