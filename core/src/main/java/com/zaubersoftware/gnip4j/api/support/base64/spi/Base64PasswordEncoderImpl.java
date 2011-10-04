package com.zaubersoftware.gnip4j.api.support.base64.spi;

import com.zaubersoftware.gnip4j.api.GnipAuthentication;

public class Base64PasswordEncoderImpl implements Base64PasswordEncoder {

	@Override
	public String encode(GnipAuthentication auth) {
		return Base64.encodeBytes(
				(auth.getUsername() + ":" + auth.getPassword()).getBytes());
	}

}
