package com.factor_y.util.crypto;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.google.common.io.BaseEncoding;


public class DesEncrypter {
	
	private static final Logger log = Logger.getLogger(DesEncrypter.class.getName());
	
    Cipher ecipher;
    Cipher dcipher;
    
    final static String ALGORITHM="PBEWithMD5AndDES";

 
    // Iteration count
    int iterationCount = 19;

    public DesEncrypter(Long seed,String passPhrase) throws DesEncrypterException {
    	log.info("Initializing DES Password Based Encrypter based on alg: "+ALGORITHM);
    	log.info("Passphrase: "+passPhrase + " len: " + passPhrase.length());
        try {
        	/*
        	Random rand = new Random(seed);
        	byte[] salt = new byte[8];
        	rand.nextBytes(salt);
            */
        	byte[] seedAsByte = seed.toString().getBytes();
        	MessageDigest algorithm = MessageDigest.getInstance("MD5");
        	algorithm.reset();
        	algorithm.update(seedAsByte);
        	byte messageDigest[] = algorithm.digest();
        	
        	if(messageDigest.length<8){
        		throw new DesEncrypterException("Seed is too short");
        	}
        	byte[] salt = new byte[8];
        	for(int i =0;i<salt.length;i++){
        		salt[i] = messageDigest[i];
        	}
            // Create the key
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance(
            		ALGORITHM).generateSecret(keySpec);
            ecipher = Cipher.getInstance(ALGORITHM);
            dcipher = Cipher.getInstance(ALGORITHM);

            // Prepare the parameter to the ciphers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            // Create the ciphers
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
            
            if (! decrypt(encrypt("test")).equals("test")) {
            	throw new RuntimeException("DesEncrypter won't work when initialized with passphrase: " + passPhrase);
            }
        } catch (java.security.InvalidAlgorithmParameterException e) {
        	throw new DesEncrypterException(e);
        } catch (java.security.spec.InvalidKeySpecException e) {
        	throw new DesEncrypterException(e);
        } catch (javax.crypto.NoSuchPaddingException e) {
        	throw new DesEncrypterException(e);
        } catch (java.security.NoSuchAlgorithmException e) {
        	throw new DesEncrypterException(e);
        } catch (java.security.InvalidKeyException e) {
        	throw new DesEncrypterException(e);
        }
    }

    public String encrypt(String str) throws DesEncrypterException {
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            return BaseEncoding.base64().encode(enc);
        } catch (javax.crypto.BadPaddingException e) {
        	throw new DesEncrypterException(e);
        } catch (IllegalBlockSizeException e) {
        	throw new DesEncrypterException(e);
        } catch (UnsupportedEncodingException e) {
        	throw new DesEncrypterException(e);
        } 
        //return null;
    }

    public String decrypt(String str) throws DesEncrypterException {
        try {
            // Decode base64 to get bytes
            byte[] dec =  BaseEncoding.base64().decode(str);

            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");
        } catch (javax.crypto.BadPaddingException e) {
        	throw new DesEncrypterException(e);
        } catch (IllegalBlockSizeException e) {
        	throw new DesEncrypterException(e);
        } catch (UnsupportedEncodingException e) {
        	throw new DesEncrypterException(e);
        } catch (java.io.IOException e) {
        	throw new DesEncrypterException(e);
        }
        //return null;
    }
    public class DesEncrypterException extends Exception{

		public DesEncrypterException() {
			super();
		}

		public DesEncrypterException(String message, Throwable cause) {
			super(message, cause);
		}

		public DesEncrypterException(String message) {
			super(message);
		}

		public DesEncrypterException(Throwable cause) {
			super(cause);
		}
		private static final long serialVersionUID = 10L;
    	
    }
}