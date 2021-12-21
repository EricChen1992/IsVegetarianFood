package thisis.vegetarian.question.mark;

import android.annotation.SuppressLint;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class IVFHashEncode {

    public static String generatePassword(String password){
        return generateHash256(password);
    }

    @SuppressLint("SimpleDateFormat")
    public static String generateToken(String userName){
        Format df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        return generateHash256(userName+date);
    }

    /**
     * 將字串轉換HASH256字串編碼
     * @param value
     * @return Hash256編碼字串
     * */
    private static String generateHash256(String value){
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = value.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.reset();
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * 將 byte 轉換為 16進制字符串
     *
     * @param digBytes 數據源
     * @return 16進制字符串
     */
    private static String bytes2Hex(byte[] digBytes) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < digBytes.length; i++) {
            tmp = (Integer.toHexString(digBytes[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
