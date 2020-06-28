package com.discordbot.maven.command.actions;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.discordbot.maven.command.CommandContext;
import com.discordbot.maven.command.Commands;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class DuelCommand implements Commands{
	
	private EventWaiter waiter;
	OffsetDateTime winningCharacterTimeStamp;
	
	public DuelCommand(EventWaiter waiter) {
		this.waiter = waiter;
	}


	@Override
	public void handle(CommandContext ctx) {
		TextChannel channel = ctx.getEvent().getChannel();
		long channelId = channel.getIdLong();
		JDA api = ctx.getJDA();
		// Grab the opponent and add it to this list
		final List<String> args = ctx.getArgs();
		
		// Making sure the opponent is there
		if (args.size() < 1 ) {
			ctx.easyMessageSend("You seem to be missing some inputs.  Did you miss something? !help for more information.");
			return;
		}
		
		// Our Events later on do not contain the ! that these args do when created.
		// For that reason we need to remove them for comparison later		
		String opponentAsMentionable = nameProcessor(args.get(0));
		String instigatorAsMentionable = nameProcessor(ctx.getMember().getAsMention());
		
		if (opponentAsMentionable.equals(ctx.getMember().getAsMention())) {
			ctx.easyMessageSend("Sorry, you can't duel yourself");
			return;
		}

		ctx.easyMessageSend(opponentAsMentionable + " type `accept` to DUEL");
		
		// We begin our chaining of EventWaiters here
		awaitAcceptance(ctx, instigatorAsMentionable, opponentAsMentionable, channelId, api);
		
		
	}
	
	
	public void awaitAcceptance(CommandContext ctx, String instigatorAsMentionable, String opponentAsMentionable, long channelId, JDA api) {	
		waiter.waitForEvent(
				GuildMessageReceivedEvent.class, 
				(event) -> {
					User user = event.getMember().getUser();
					
					if (!user.isBot() && event.getMember().getAsMention().equals(opponentAsMentionable) && event.getMessage().getContentRaw().equalsIgnoreCase("accept")) {
						return true;
					} return false;
				}, 
				(event) -> {
					String winningCharacter = characterGenerator();
					ctx.easyMessageSend(opponentAsMentionable + " has accepted the duel.  Get Ready");
					ctx.getChannel().sendMessage("3...").queueAfter(delayGenerator(1), TimeUnit.MILLISECONDS);
					ctx.getChannel().sendMessage("2...").queueAfter(delayGenerator(3), TimeUnit.MILLISECONDS);
					ctx.getChannel().sendMessage("1...").queueAfter(delayGenerator(5), TimeUnit.MILLISECONDS);
					ctx.getChannel().sendMessage(winningCharacter).queueAfter(delayGenerator(7), TimeUnit.MILLISECONDS);
					
					awaitWinnerResponse(event, instigatorAsMentionable, opponentAsMentionable, channelId, api, winningCharacter);
					},
				20, TimeUnit.SECONDS,
				() -> {
					ctx.easyMessageSend("I stopped listening for the opponent");
				}
				);
	}

	private void awaitWinnerResponse(GuildMessageReceivedEvent event, String instigatorAsMentionable, String opponentAsMentionable, long channelId, JDA api, String winningCharacter) {
		waiter.waitForEvent(GuildMessageReceivedEvent.class, 
				(e) -> {
					User user = e.getMember().getUser();
					
					if ((!user.isBot() && e.getMessage().getContentRaw().equals(winningCharacter)) && (e.getMember().getAsMention().equals(instigatorAsMentionable) || e.getMember().getAsMention().equals(opponentAsMentionable))) {
						return true;
					} return false;
				}, 
				(e) -> {
					TextChannel channel = api.getTextChannelById(channelId);
					
					channel.sendMessage(e.getMember().getAsMention() + " is the winner!").queue(message -> message.delete().queueAfter(20, TimeUnit.SECONDS));
				},
				20, TimeUnit.SECONDS,
				() -> {
					TextChannel channel = api.getTextChannelById(channelId);
					channel.sendMessage("Nobody shot, so I'm done listening").queue(message -> message.delete().queueAfter(20, TimeUnit.SECONDS));
				});
	}
	
	
	private int delayGenerator(int delaySequence) {
		int min = 1000 * delaySequence;
		int max = 1500 * delaySequence;
		
		int randomNumber = (int) (Math.random() * (max - min + 1) + min);
		return randomNumber;
	}
	
	private String nameProcessor(String nameToProcess) {
		return nameToProcess.replace("!", "");
	}
	
	private String characterGenerator() {
		final String alphabet = "abcdefghijklmnopqrstuvwxyz";
		final int alphabetLength = alphabet.length();
		
		Random randomNumber = new Random();
		char returnLetter = alphabet.charAt(randomNumber.nextInt(alphabetLength));
		String returnString = String.valueOf(returnLetter);
		return returnString;
	}
	

	@Override
	public String getName() {
		return "duel";
	}

	@Override
	public String getHelp() {
		return "Duel another user";
	}

}
