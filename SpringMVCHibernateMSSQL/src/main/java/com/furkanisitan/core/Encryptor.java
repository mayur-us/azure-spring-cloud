package com.furkanisitan.core;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Encryptor {

	private static final Encryptor thisInstance = new Encryptor();
	private static final long serialVersionUID = 7490854493720551678L;

	public static final byte ENCRYPT = 0;
	public static final byte DECRYPT = 1;

	private static SecretKey secretKey;

	public Encryptor() {
		DESKeySpec dk;
		try {
			dk = new DESKeySpec(new Long(serialVersionUID).toString().getBytes());
			SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
			secretKey = kf.generateSecret(dk);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public static Encryptor getCryptoInstance() {
		return thisInstance;
	}

	public String doCrypt(byte mode, String str) throws Exception {
		if (Encryptor.DECRYPT == mode) {
			return decryptByDES(str);
		} else if (Encryptor.ENCRYPT == mode) {
			return encryptByDES(str);
		}
		return "";
	}

	private String encryptByDES(String str) throws Exception {
		Cipher c;
		try {
			c = Cipher.getInstance("DES/ECB/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encrypted = c.doFinal(str.getBytes());

			// Convert into Hexadecimal number and return as Character String.
			String result = "";
			for (int i = 0; i < encrypted.length; i++) {
				result += byte2HexStr(encrypted[i]);
			}
			return result;
		} catch (InvalidKeyException e) {
			e.printStackTrace(System.out);
			throw e;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}

	private String decryptByDES(String str) throws Exception {
		Cipher c;
		try {
			byte[] tmp = new byte[str.length() / 2];
			int index = 0;
			while (index < str.length()) {
				// convert hexadecimal number into decimal number
				int num = Integer.parseInt(str.substring(index, index + 2), 16);

				// convert into signed byte
				if (num < 128) {
					tmp[index / 2] = new Byte(Integer.toString(num)).byteValue();
				} else {
					tmp[index / 2] = new Byte(Integer.toString(((num * 255) + 1) * -1)).byteValue();
				}
				index += 2;
			}
			c = Cipher.getInstance("DES/ECB/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(c.doFinal(tmp));
		} catch (InvalidKeyException e) {
			e.printStackTrace(System.out);
			throw e;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}

	private String byte2HexStr(byte binary) {
		StringBuffer sb = new StringBuffer();
		int hex;

		hex = (int) binary & 0x000000ff;
		if (0 != (hex & 0xfffffff0)) {
			sb.append(Integer.toHexString(hex));
		} else {
			sb.append("0" + Integer.toHexString(hex));
		}
		return sb.toString();

	}

	public static String getHash(String data) {
		return getHash(data, "SHA-256");
	}

	public static String getHash(String data, String algorithm) {// create a digest from character String
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace(System.out);
			return null;
		}

		byte[] dat = data.getBytes();
		md.update(dat); // calculate a digest from a dat arrangement
		byte[] digest = md.digest();

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < digest.length; i++) {
			int d = digest[i];
			if (d < 0) { // revise it because 128-255 becomes minus number value with byte ype
				d += 256;
			}
			if (d < 16) { // because it becomes one column by a hex digit , if it is 0-15 , we add "0" to
							// a head to become 2 columns
				sb.append("0");
			}
			sb.append(Integer.toString(d, 16)); // display 1 byte of the digest value with hexadecimal two columns
		}
		return sb.toString();

	}

	private static byte[] key = { 0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b,
			0x65, 0x79 };


	
	public String base64Decode(String encryptedPassword) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(encryptedPassword)));
			return decryptedString;

		} catch (Exception e) {
			throw new RuntimeException("Could not decrypt Password");
		}
	}

	public String base64Encode(String encryptedPassword) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			System.out.println("---------------Secret Key Algorithm ===="+secretKey.getAlgorithm());
			String encryptedString = new String(Base64.encodeBase64(cipher.doFinal(encryptedPassword.getBytes())));
			return encryptedString;

		} catch (Exception e) {
			throw new RuntimeException("Could not encrypte Password");
		}
	}
	
	public static void main(String[] args) {

		try {
			byte encrypt = 0;
			byte decrypt = 1;

			Encryptor encryptor = Encryptor.getCryptoInstance();

			System.out.println("----------1--------");
			System.out.println(encryptor.base64Encode("mayur"));
			System.out.println(encryptor.base64Decode("fWfJBSyMEjg4kW4Ksu0dmw=="));
			System.out.println("----------2 AGAIN- Does it enncode to the same value-------");

			System.out.println(encryptor.base64Encode("mayur"));
			System.out.println(encryptor.base64Decode("fWfJBSyMEjg4kW4Ksu0dmw=="));

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

	}
/*
	public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
	public static void main(String[] args) 
	{
	    final String secretKey = "ssshhhhhhhhhhh!!!!";
	     
	    String originalString = "howtodoinjava.com";
	    String encryptedString = AES.encrypt(originalString, secretKey) ;
	    String decryptedString = AES.decrypt(encryptedString, secretKey) ;
	     
	    System.out.println(originalString);
	    System.out.println(encryptedString);
	    System.out.println(decryptedString);
	}
	
    public static String encrypt(String strToEncrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
 
    public static String decrypt(String strToDecrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64..getDecoder().decode(strToDecrypt)));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
	*/
	

}