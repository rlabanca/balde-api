package org.baldeapi.v1.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	public static String make(String senha){
		if (senha == null) {
			return null;
		}
        String sen = "";  
        MessageDigest md = null;  
        try {  
            md = MessageDigest.getInstance("MD5");  
        } catch (NoSuchAlgorithmException e) {
        }  
        BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));  
        sen = hash.toString(16);              
        return sen;  
    }
	
}
