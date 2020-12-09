package com.yachugak.topla.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import com.yachugak.topla.exception.GeneralExceptions;

public class SHA256 {

	private String salt;

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public SHA256() {
		this.setSalt("EmoCJhnlOzMId6QO21qLUw==");
	}

	public String generateSalt() {
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			byte[] bytes = new byte[16];
			random.nextBytes(bytes);
			String salt = new String(Base64.getEncoder().encode(bytes));
			return salt;

		} catch (Exception e) {
			throw new GeneralExceptions("비밀번호 암호화 과정에서 문제가 생겼습니다.");
		}
	}
	
	public String getEncrpyt(String source) {
		MessageDigest md;
		
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new GeneralExceptions("암호화 오류");
		}
		
		md.update(this.salt.getBytes());
		md.update(source.getBytes());
		String encrpytedPassword = String.format("%064x", new BigInteger(1, md.digest()));
		return encrpytedPassword;
	}
}
