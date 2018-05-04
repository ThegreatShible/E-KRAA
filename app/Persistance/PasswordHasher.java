package Persistance;


import org.apache.commons.codec.digest.DigestUtils;

public class PasswordHasher {
    public static String md5Hash(String str) {
        return DigestUtils.md5Hex(str);
    }
}
