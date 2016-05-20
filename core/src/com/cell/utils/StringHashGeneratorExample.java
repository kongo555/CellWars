package com.cell.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by kongo on 30.03.16.
 */
public class StringHashGeneratorExample {

    public static void main(String[] args) {
        try {
            String inputString = "a";
            System.out.println("Input String: " + inputString);

            String sha256Hash = SecurityUtils.generateSHA256(inputString);
            System.out.println("SHA-256 Hash: " + sha256Hash);

            System.out.println("SHA-256 Hash: " + hash(inputString));

            System.out.println("SHA-256 Hash: " + sha256(inputString));
        } catch (HashGenerationException ex) {
            ex.printStackTrace();
        }
    }

    public static String hash(String password) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            //String salt = "some_random_salt";
            //String passWithSalt = password + salt;
            String passWithSalt = password;
            byte[] passBytes = passWithSalt.getBytes();
            byte[] passHash = sha256.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< passHash.length ;i++) {
                sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
            }
            String generatedPassword = sb.toString();
            return generatedPassword;
        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        return null;
    }

    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

}