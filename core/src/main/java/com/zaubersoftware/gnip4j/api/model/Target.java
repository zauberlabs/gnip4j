package com.zaubersoftware.gnip4j.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Target {

	@JsonProperty("up_to_id")
	public String upToId;
	
	public String getUpToId() {
		return upToId;
	}

	public void setUpToId(String upToId) {
		this.upToId = upToId;
	}

}
