package com.discordbot.maven.command.actions;

import java.util.List;

import com.discordbot.maven.command.CommandContext;
import com.discordbot.maven.command.Commands;
import com.discordbot.maven.discorduser.JDBCDiscordUserDAO;
import com.discordbot.maven.economy.EconomyDataSourceCreator;

public class GambleCommand implements Commands{

	private JDBCDiscordUserDAO discordDao = EconomyDataSourceCreator.main();
	int dollarsToGamble;
	
	@Override
	public void handle(CommandContext ctx) {
		// TODO Auto-generated method stub
		int dollarsAvailable = discordDao.checkFundsForSpecificUser(ctx.getMember());
		
		
		// Grab the amount of dollars to gamble, making sure its present
		final List<String> args = ctx.getArgs();
						
		// Making sure the dollars are present
		if (args.size() < 1 ) {
			ctx.easyMessageSend("You seem to be missing some inputs.  Did you miss something? !help for more information.");
			return;
		}
			
		try {
			dollarsToGamble = Integer.parseInt(ctx.argParser());
		} catch (NumberFormatException e) {
			ctx.easyMessageSend("That doesn't look like a dollar amount to me.  Try re-entering the command.");
			return;
		}
		
		if (dollarsToGamble <= dollarsAvailable) {
			int randomWinningNumber = randomNumberGenerator();
			if (winningNumberChecker(randomWinningNumber)) {
				winnerProcessing(dollarsToGamble, dollarsAvailable, ctx);
				return;
			} else {
				loserProcessing(dollarsToGamble, dollarsAvailable, ctx);
				return;
			}
		} else {
			ctx.easyMessageSend("You don't have the funds available");
			return;
		}
		
		
	}

	@Override
	public String getName() {
		return "gamble";
	}

	@Override
	public String getHelp() {
		return "Gamble your dollars! 5% Odds - 12x Return.  Feeling lucky?";
	}
	
	private int randomNumberGenerator() {
		int min = 1;
		int max = 1000;
		
		int randomNumber = (int) (Math.random() * (max - min + 1) + min);
		return randomNumber;
	}
	
	private boolean winningNumberChecker(int randomWinningNumber) {
		if (randomWinningNumber < 50) {
			return true;
		}
		return false;
	}
	
	private void winnerProcessing(int dollarsToGamble, int dollarsAvailable, CommandContext ctx) {
		int winnings = dollarsToGamble * 12;
		int newBalance = dollarsAvailable + winnings;
		ctx.easyMessageSend("You won $" + winnings + "! Your new balance is $" + newBalance);
		discordDao.addDollarsToUser(ctx.getMember(), winnings);	
	}
	
	private void loserProcessing(int dollarsToGamble, int dollarsAvailable, CommandContext ctx) {
		int newBalance = dollarsAvailable - dollarsToGamble;
		ctx.easyMessageSend("You lost. Your new balance is $" + newBalance);
		discordDao.removeDollarsFromUser(ctx.getMember(), dollarsToGamble);	
	}
	
}