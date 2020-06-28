package com.discordbot.maven.discorduser;

import java.util.ArrayList;

import net.dv8tion.jda.api.entities.Member;

public interface DiscordUserDAO {

	public void createNewUser(Member member);
	
	public DiscordUser findUserByUserId(long id);
	
	public void addDollarsForVoiceTime(Member user);
	
	public void removeDollarsFromUser(Member user, int amountOfDollars);
	
	public void addDollarsToUser(Member user, int amountOfDollars);
	
	public ArrayList<DiscordUser> dollarCheckByGuildId(String guildId);
	
	public int checkFundsForSpecificUser(Member user);
	
	public boolean checkIfUserExists(Member user);
	
}
