package caiji.util;

import java.security.*;
import java.io.*;

public class x
{
    public static String a(String a) {
        try {
            final MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(a.getBytes("UTF-8"));
            a = a(instance.digest());
            return a;
        }
        catch (NoSuchAlgorithmException ex) {
//            u.e("MD5", "MD5 NoSuchAlgorithmException");
            ex.printStackTrace();
            return "";
        }
        catch (UnsupportedEncodingException ex2) {
//            u.e("MD5", "to md5 string not support UTF-8 to bytes");
            ex2.printStackTrace();
            return "";
        }
    }
    
    public static String a(final byte[] array) {
        final StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < array.length; ++i) {
            final int n = array[i] >> 4 & 0xF;
            final int n2 = array[i] & 0xF;
            char c;
            if (n > 9) {
                c = (char)(n - 10 + 97);
            }
            else {
                c = (char)(n + 48);
            }
            sb.append(c);
            char c2;
            if (n2 > 9) {
                c2 = (char)(n2 - 10 + 97);
            }
            else {
                c2 = (char)(n2 + 48);
            }
            sb.append(c2);
        }
        return sb.toString();
    }
    
    public static String b(String a) {
        try {
            final MessageDigest instance = MessageDigest.getInstance("SHA1");
            instance.update(a.getBytes("UTF-8"));
            a = a(instance.digest());
            return a;
        }
        catch (NoSuchAlgorithmException ex) {
//            u.e("MD5", "MD5 NoSuchAlgorithmException");
            ex.printStackTrace();
            return "";
        }
        catch (UnsupportedEncodingException ex2) {
//            u.e("MD5", "to md5 string not support UTF-8 to bytes");
            ex2.printStackTrace();
            return "";
        }
    }
}
