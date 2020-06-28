package com.discordbot.maven.discorduser;

public class DiscordUser {

	private Long userId;
	private String name;
	private int dollars;
	private int voiceTime;
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getDollars() {
		return dollars;
	}
	
	public void setDollars(int dollars) {
		this.dollars = dollars;
	}
	
	public int getVoiceTime() {
		return voiceTime;
	}
	
	public void setVoiceTime(int voiceTime) {
		this.voiceTime = voiceTime;
	}
	
	
	
	
	
}
