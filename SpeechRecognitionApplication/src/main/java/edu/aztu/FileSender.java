package edu.aztu;

import java.io.*;
import java.net.*;

public class FileSender {
    public static void main(String[] args) {
        try (Socket socket = new Socket("192.168.1.2", 5000); // Serverin IP və portu
             FileInputStream fis = new FileInputStream("C:\\Users\\99470\\Desktop\\Magistr\\arayis.jpg");
             BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream())) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) > 0) {
                bos.write(buffer, 0, bytesRead);
            }
            System.out.println("Fayl göndərildi.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

