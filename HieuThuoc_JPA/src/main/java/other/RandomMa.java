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
        return UUID.randomUUID().toString();
    }
    public static String maPhieuDatAuto() {
        return UUID.randomUUID().toString();
    }
    public static String maPhieuNhapAuto() {
        return UUID.randomUUID().toString();
    }
    public static String maKHAuto() {
        return UUID.randomUUID().toString();
    }



}
