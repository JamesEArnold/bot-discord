package com.discordbot.maven.command.actions;

import java.util.ArrayList;

import com.discordbot.maven.command.CommandContext;
import com.discordbot.maven.command.Commands;
import com.discordbot.maven.discorduser.DiscordUser;
import com.discordbot.maven.discorduser.JDBCDiscordUserDAO;
import com.discordbot.maven.economy.EconomyDataSourceCreator;

public class DollarCheckerCommand implements Commands {

	private JDBCDiscordUserDAO discordDao = EconomyDataSourceCreator.main();
	
	// TODO Consider adding the DAO to the CommandManager so we can pass it to other commands as well
	@Override
	public void handle(CommandContext ctx) {
		ArrayList<DiscordUser> users = discordDao.dollarCheckByGuildId(ctx.getGuild().getId());
		
		for (DiscordUser user : users) {
			ctx.easyMessageSend(user.getName() + " has $" + user.getDollars() + " dollars");
		}
		
	}

	@Override
	public String getName() {
		return "dollarchecker";
	}

	@Override
	public String getHelp() {
		return "Check the guild's dollars";
	}
	
	

}
