package edu.aztu.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum FileNumber {

    FIRST(1,"FIRST"), SECOND(2,"SECOND"),
    THIRTH(3,"THIRT"), FORTH(4,"FORTH"),
    FIFTH(5,"FIFTH"), SIXTH(6,"SIXTH"),
    SEVENTH(7,"SEVENTH"), EIGHTH(8,"EIGHT"),
    NINTH(9,"NINTH"), TENTH(10,"TENTH"),
    ELEVENTH(11,"ELEVENTH"), TWELFTH(12,"TWELFTH"),
    THIRTEENTH(13,"THIRTEENTH"), FOURTEENTH(14,"FOURTEENTH"),
    FIFTEENTH(15,"FIFTEENTH");

    final int value;
    final String number;
    FileNumber(int value,String number){
        this.value = value;
        this.number = number;
    }

    public int getValue() {
        return value;
    }

    public String getNumber() {
        return number;
    }

    public static List<String> getFileNumber(){
        List<FileNumber> fileNumber = Arrays.stream(FileNumber.values()).toList();
        List<String> strFileNumber = new LinkedList<>();
        System.out.println(fileNumber);
        for (FileNumber i : fileNumber){
            strFileNumber.add(i.getNumber());
        }
        return strFileNumber;
    }
}
