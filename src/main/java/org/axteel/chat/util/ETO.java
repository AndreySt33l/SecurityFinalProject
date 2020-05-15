package org.axteel.chat.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.axteel.chat.security.cipher.AES;
import org.axteel.chat.security.cipher.RSA;
import org.modelmapper.ModelMapper;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
* ETO: Encrypted Transfer Object
 *      Helps to transfer Encrypted JSON data
* @author : Andrey Stalnoy
* */
public class ETO {
    private String publicSessionId;
    private String encryptedKey;
    private String publicResponseKey;
    private String content;

    public ETO() { }

    public ETO(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.content = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public ETO(String object) {
        this.content = object;
    }

    public String getPublicResponseKey() {
        return publicResponseKey;
    }

    public void setPublicResponseKey(String publicResponseKey) {
        this.publicResponseKey = publicResponseKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEncryptedKey() {
        return encryptedKey;
    }

    public void setEncryptedKey(String encryptedKey) {
        this.encryptedKey = encryptedKey;
    }

    public Object decrypt(PrivateKey privateKey, Class model){
        try {
            this.content = RSA.decrypt(content, privateKey);
            ObjectMapper objectMapper = new ObjectMapper();
            Object con = objectMapper.readValue(this.content, model);

            ModelMapper mapper = new ModelMapper();
            Object result = mapper.map(con, model);

            return con;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*public Object decrypt(String privateKey, Class model) {
        try {
            byte[] key = Base64.getUrlDecoder().decode(privateKey);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(key);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey pk = kf.generatePrivate(spec);

            return decrypt(pk, model);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    public static ETO encrypt(Object object, String pubKey) {
        try {
            /* 1. Генерируем AES ключ */
            SecretKey aesKey = KeyGenerator.getInstance("AES").generateKey();
            String aesString = Base64.getUrlEncoder().encodeToString(aesKey.getEncoded());

            /* 2. Создаем JSON на основе object */
            String jsonObject = new ObjectMapper().writeValueAsString(object);

            /* 3. Шифруем ключом AES json */
            AES aes = new AES();
            String cipheredText = aes.encrypt(jsonObject, aesKey);

            /* 4. Шифруем ключем RSA ключ AES */
            PublicKey key = decodePublicKey(pubKey);
            String encryptedKey = RSA.encrypt(aesString, key);

            /* 5. Записываем инфу в поля*/
            ETO transferObject = new ETO();
            transferObject.setContent(cipheredText);
            transferObject.setEncryptedKey(encryptedKey);
            return transferObject;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object decrypt (Class cls, String privateKey) {
        try {
            /* 1. Расшифровываем AES ключ используя RSA private key */
            //String aesKeyString = RSA.decrypt(this.key, decodePrivateKey(privateKey));
            String aesKeyString = RSA.decrypt(this.encryptedKey, RSA.parsePrivateKey(privateKey));
            byte[] bytesKey = Base64.getUrlDecoder().decode(aesKeyString);
            SecretKey originalKey = new SecretKeySpec(bytesKey, 0, bytesKey.length, "AES");

            /* 2. Расшифрованным AES ключом расшифровываем контент */
            String objectString = new AES().decrypt(content, originalKey);

            /* 3. Расшифрованный контент мы мапим использую класс*/
            Object result = new ObjectMapper().readValue(objectString, cls);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static PrivateKey decodePrivateKey(String key) throws Exception {
        byte[] keyBytes = Base64.getUrlDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return kf.generatePrivate(spec);
    }

    private static PublicKey decodePublicKey(String key) throws Exception {
        byte[] rsaKey = Base64.getUrlDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(rsaKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public String getPublicSessionId() {
        return publicSessionId;
    }

    public void setPublicSessionId(String publicSessionId) {
        this.publicSessionId = publicSessionId;
    }

    @Override
    public String toString() {
        return "EncryptedTransferObject\n{" +
                "\n\tcontent=" + content +
                "\n\tkey=" + encryptedKey +
                "\n\tpublicSessionId=" + publicSessionId +
                "\n\tpublicResponseKey=" + publicResponseKey +
                "\n}";
    }
}
