package org.axteel.chat.security.cipher;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RSA {
    /*Brute-Foce modular*/
    public static BigInteger modularInverse(BigInteger a, BigInteger m) {
        BigInteger inv = new BigInteger("0");

        // Within the range [0, m-1]
        while (inv.compareTo(m) < 0) {
            // We have to check a * a^(-1) mod m == 1 true of false
            if (a.multiply(inv).mod(m).equals(BigInteger.ONE))
                return inv;

            // Check the next value in a brute force
            inv = inv.add(BigInteger.ONE);
        }

        return null;
    }

    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048, new SecureRandom());
            KeyPair pair = generator.generateKeyPair();
            return pair;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static KeyPair getKeyPairFromKeyStore() throws Exception {
        InputStream ins = RSA.class.getResourceAsStream("/keystore.jks");

        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(ins, "s3cr3t".toCharArray());
        KeyStore.PasswordProtection keyPassword =
                new KeyStore.PasswordProtection("s3cr3t".toCharArray());

        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry("mykey", keyPassword);

        java.security.cert.Certificate cert = keyStore.getCertificate("mykey");
        PublicKey publicKey = cert.getPublicKey();
        PrivateKey privateKey = privateKeyEntry.getPrivateKey();

        return new KeyPair(publicKey, privateKey);
    }

    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(UTF_8));

        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);

        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decryptCipher.doFinal(bytes), UTF_8);
    }

    public static PublicKey parsePublicKey(String publicKey) {
        try {
            publicKey = publicKey.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
            System.out.println(publicKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
            return kf.generatePublic(keySpecX509);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PrivateKey parsePrivateKey(String privateKey) {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");

            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getUrlDecoder().decode(privateKey));
            return kf.generatePrivate(keySpecPKCS8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String... argv) throws Exception {
        /*System.out.println("Generating Private and Public keys");
        KeyPair pair = generateKeyPair();
        System.out.println("Private: \n" + pair.getPrivate());
        System.out.println("Public: \n" + pair.getPublic());

        //Our secret message
        System.out.println("Please, enter your message below:");
        String message = new Scanner(System.in).nextLine();

        System.out.println("The secret message: \n\t" + message);
        //Encrypt the message
        String cipherText = encrypt(message, pair.getPublic());
        System.out.println("Ciphered Text : \n\t" + cipherText);

        //Now decrypt it
        String decipheredMessage = decrypt(cipherText, pair.getPrivate());
        System.out.println("Decrypting...");
        System.out.println("Deciphered Message: \n\t" + decipheredMessage);*/

    }
}
