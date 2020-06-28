package com.discordbot.maven.command.actions;

import java.util.concurrent.TimeUnit;

import com.discordbot.maven.command.CommandContext;
import com.discordbot.maven.command.Commands;

import net.dv8tion.jda.api.JDA;


public class PingCommand implements Commands {

	@Override
	public void handle(CommandContext ctx) {
		JDA jda = ctx.getJDA();
		
		jda.getRestPing().queue(
				(ping) -> ctx.getChannel()
				.sendMessageFormat("Reset Ping: %sms\nWS ping: %sms", ping, jda.getGatewayPing()).queue(message -> message.delete().queueAfter(20, TimeUnit.SECONDS)));

	}


	@Override
	public String getName() {
		return "ping";
	}
	
	@Override
	public String getHelp() {
		return "Shows the current ping from the bot to the Discord server";
	}

}
