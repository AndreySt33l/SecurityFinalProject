package org.axteel.chat.security.cipher;

import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AES {

	public String encrypt(String plainText, SecretKey key) {

		//the key is a 64 bits long sequence in DES
		//we are going to deal with byte arrays
		byte[] cipherText = null;

		try {

			//we use Data Encryption Standard (AES)
			Cipher encryptionCipher = Cipher.getInstance("AES");
			//we use DES for encryption in this case
			encryptionCipher.init(Cipher.ENCRYPT_MODE, key);

			//first convert the plaintext into bytes
			byte[] bytes = plainText.getBytes();
			//this is whey the encryption happens
			cipherText = encryptionCipher.doFinal(bytes);
			//we want to represent and encode the byte sequence as string
			cipherText = Base64.getEncoder().encode(cipherText);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return new String(cipherText);
	}

	public String decrypt(String cipherText, SecretKey key) {

		byte[] utfDecrypted = null;

		try {

			Cipher decryptionCipher = Cipher.getInstance("AES");
			decryptionCipher.init(Cipher.DECRYPT_MODE, key);

			//Base64 is an encoding to represent any byte sequence by a sequence of printable characters 
			//we want to deal with bytes
			byte[] bytedDecrypted = Base64.getDecoder().decode(cipherText.getBytes());
			//this is where the decryption happens
			utfDecrypted = decryptionCipher.doFinal(bytedDecrypted);
			
			return new String(utfDecrypted,"UTF8");
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void main(String[] args) throws Exception{
		String plainText = "This is a secret message let's encrypt it!";

		//the private key is a 64 bits long sequence
		SecretKey key = KeyGenerator.getInstance("AES").generateKey();

		AES aes = new AES();

		String cipherText = aes.encrypt(plainText, key);

		System.out.println("Encrypted text: "+ cipherText);

		System.out.println("Decrypted text: "+ aes.decrypt(cipherText, key));
	}
}
