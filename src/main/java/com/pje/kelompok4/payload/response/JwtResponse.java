package com.pje.kelompok4.payload.response;

import java.util.List;

public class JwtResponse {
	private String uniqueId;
	private String username;
	private List<String> roles;
	private String type = "pje";
	private String token;

	public JwtResponse(String accessToken, String uniqueId, String username, List<String> roles) {
		this.uniqueId = uniqueId;
		this.username = username;
		this.roles = roles;
		this.token = accessToken;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}
}
