package manager;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.google.common.base.Charsets;

import lang.Lang;
import lang.Messages;
import utils.HCIUtils;
import utils.MiscUtils;

public abstract class CipherManager {
	
	// STATIC FINAL FIELDS
	private static final String CIPHER_ALGORITHM = "AES";
	private static final String KEY_ALGORITHM = "AES";
	private static final String SECRET_KEY = "B5EF45741094B76E"; // B2A7422A86C0D704
	
	// STATIC FIELDS
	private static Cipher cipherEncryptInstance = null;
	private static Cipher cipherDecryptInstance = null;
	private static SecretKey secretKeyInstance= null;
	
	// STATIC METHODS
	private static SecretKey getSecretKeyInstance() {
		if(secretKeyInstance == null){
			secretKeyInstance = new SecretKeySpec(SECRET_KEY.getBytes(), KEY_ALGORITHM);
		}
		return secretKeyInstance;
	}
	
	private static Cipher getCipherEncryptInstance() {
		if(cipherEncryptInstance == null){
			try {
				cipherEncryptInstance = Cipher.getInstance(CIPHER_ALGORITHM);
				cipherEncryptInstance.init(Cipher.ENCRYPT_MODE, getSecretKeyInstance());
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
				HCIUtils.showException(e, true, true, Messages.getString(Lang.LOG_MESSAGE_ERROR_CIPHER_INITIALIZATION_ENCRYPTER));
			}
		}
		return cipherEncryptInstance;
	}
	
	private static Cipher getCipherDecryptInstance() {
		if(cipherDecryptInstance == null){
			try {
				cipherDecryptInstance = Cipher.getInstance(CIPHER_ALGORITHM);
				cipherDecryptInstance.init(Cipher.DECRYPT_MODE, getSecretKeyInstance());
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
				HCIUtils.showException(e, true, true, Messages.getString(Lang.LOG_MESSAGE_ERROR_CIPHER_INITIALIZATION_DECRYPTER));
			}
			
		}
		return cipherDecryptInstance;
	}
	
	public static String encrypt(String input){
		byte[] result = {};
		try {
			result = getCipherEncryptInstance().doFinal(input.getBytes(Charsets.UTF_8));
			result = Base64.encodeBase64(result);
		} catch (IllegalBlockSizeException | BadPaddingException | NullPointerException e) {
			HCIUtils.showException(e, true, true, Messages.getString(Lang.LOG_MESSAGE_ERROR_CIPHER_ENCRYPTION));
		}
		return new String(result);
	}
	
	public static String decrypt(String input){
		byte[] result = {};
		try {
			result = Base64.decodeBase64(input.getBytes(Charsets.UTF_8));
			result = getCipherDecryptInstance().doFinal(result);
		} catch (IllegalBlockSizeException | BadPaddingException | NullPointerException e) {
			HCIUtils.showException(e, true, true, Messages.getString(Lang.LOG_MESSAGE_ERROR_CIPHER_DECRYPTING), MiscUtils.getValueOfObject(input));
		}
		return new String(result);
	}

}
