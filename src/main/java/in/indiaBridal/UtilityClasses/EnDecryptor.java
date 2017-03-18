package in.indiaBridal.UtilityClasses;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;

import org.apache.log4j.Logger;

public class EnDecryptor 
{
	public static void main(String[] args) {
		EnDecryptor decObj = new EnDecryptor();
		logger.debug(decObj.encryption("mncmWj7tVS9IjWDH0PrT9Q==",false));
	}
	final static Logger logger = Logger.getLogger(EnDecryptor.class); 

	private static byte[] key = { 0x43, 0x56, 0x53, 0x5f, 0x52, 0x65, 0x74, 0x61, 0x69, 0x6c, 0x49, 0x56, 0x52, 0x4b, 0x65, 0x79 };
	
	public String encryption(String value, Boolean encrypt)
	{
		try
		{	
			String cipherText,decryptedText;
			final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			if(encrypt)
			{
				//encryption
				aesCipher.init(Cipher.ENCRYPT_MODE,secretKey);
				byte[] byteDataToEncrypt = value.getBytes();
				byte[] byteCipherText = aesCipher.doFinal(byteDataToEncrypt); 
				cipherText = new BASE64Encoder().encode(byteCipherText);
				return cipherText;
			}
			else
			{
			//decryption
				byte[] byteEncryptedCipherText = new BASE64Decoder().decodeBuffer(value);
				aesCipher.init(Cipher.DECRYPT_MODE,secretKey,aesCipher.getParameters());
				byte[] byteDecryptedText = aesCipher.doFinal(byteEncryptedCipherText); 
				decryptedText = new String(byteDecryptedText); 
					return decryptedText;
			}
		}
		catch(Exception e)
		{
			logger.debug("Exception occurred while encyrpt or decrypting");
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug(" statck trace = "+stack);
			return null;
		}
	}
}
