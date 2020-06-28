package com.discordbot.maven.economy;

import java.util.ArrayList;
import java.util.TimerTask;

import com.discordbot.maven.discorduser.JDBCDiscordUserDAO;

import net.dv8tion.jda.api.entities.Member;

public class EconomyAddDollars extends TimerTask{
	
	ArrayList<Member> activeVoiceMembers;
	private JDBCDiscordUserDAO discordDAO = EconomyDataSourceCreator.main();

	public EconomyAddDollars(ArrayList<Member> activeVoiceMembers) {
		this.activeVoiceMembers = activeVoiceMembers;
	}
	
	@Override
	public void run() {
		// We'll increase our jackpot here for the lottery
		discordDAO.jackpotIncreaseForTime();
		
		// This method iterates through our current active voice members and
		// will create the user in the database if they're not there.
		// Or will assign dollars if they're already in the database.
		for (Member member : activeVoiceMembers) {
				System.out.println(member.getEffectiveName() + " is currently on the active member list");
				
				if (!discordDAO.checkIfUserExists(member)) {
					discordDAO.createNewUser(member);
				} else {
					discordDAO.addDollarsForVoiceTime(member);
				}
		}	
	}
	
}