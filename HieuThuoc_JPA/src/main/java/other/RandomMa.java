package other;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.UUID;


public class RandomMa {
    public static String getRandomId() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = 5;

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }
    public static String maHoaDonAuto() {
        // Generate IDs in format HD-yyyyMMdd-XXXXX (year, month, day, random 5 chars)
        java.time.LocalDate now = java.time.LocalDate.now();
        String datePart = String.format("%d%02d%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        return "HD-" + datePart + "-" + getRandomId();
    }
    public static String maPhieuDatAuto() {
        // Generate IDs in format PD-yyyyMMdd-XXXXX
        java.time.LocalDate now = java.time.LocalDate.now();
        String datePart = String.format("%d%02d%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        return "PD-" + datePart + "-" + getRandomId();
    }
    public static String maPhieuNhapAuto() {
        // Generate IDs in format PN-yyyyMMdd-XXXXX
        java.time.LocalDate now = java.time.LocalDate.now();
        String datePart = String.format("%d%02d%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        return "PN-" + datePart + "-" + getRandomId();
    }
    public static String maKHAuto() {
        // Generate IDs in format KH-yyyyMMdd-XXXXX
        java.time.LocalDate now = java.time.LocalDate.now();
        String datePart = String.format("%d%02d%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        return "KH-" + datePart + "-" + getRandomId();
    }



}
