package com.discordbot.maven.command;

import java.util.List;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandContext implements EventContext {
	
	/*
	 * We need our event and args to gain context surrounding the event
	 */
	private final GuildMessageReceivedEvent event;
	private final List<String> args;
	
	public CommandContext(GuildMessageReceivedEvent event, List<String> args) { 
		this.event = event;
		this.args = args;
	}
	
	public List<String> getArgs() {
		return this.args;
	}

	public String getName() {
		return this.getName();
	}

	@Override
	public Guild getGuild() {
		return this.getEvent().getGuild();
	}

	@Override
	public GuildMessageReceivedEvent getEvent() {
		return this.event;
	}


	public void handle(Commands ctx) {
		
	}

	public String getHelp() {
		return null;
	}
	
	public String argParser() {
		String parsedArg = null;
		for (int i=0; i < this.args.size(); i++) {
			if (i == 0) {
				parsedArg = this.args.get(i);
			} else if (i > 0) {
				parsedArg = parsedArg + " " + this.args.get(i) ;
			}
		} return parsedArg;
	}
	
	public void messageCleanup() {
		this.event.getMessage().delete().queueAfter(20, TimeUnit.SECONDS);
	}
	
	public void easyMessageSend(String botResponse) {
		this.event.getChannel().sendMessage(botResponse).queue(message -> message.delete().queueAfter(20, TimeUnit.SECONDS));
	}
	
	public void easyMessageSendFormatted(String botResponse, List<String>args) {
		this.event.getChannel().sendMessageFormat(botResponse, args).queue(message -> message.delete().queueAfter(20, TimeUnit.SECONDS));
	}
	
	

	
}
