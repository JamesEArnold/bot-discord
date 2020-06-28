package com.discordbot.maven.command.actions;

import java.util.List;

import com.discordbot.maven.command.CommandContext;
import com.discordbot.maven.command.Commands;
import com.discordbot.maven.quickstart.CommandManager;
import com.discordbot.maven.quickstart.Config;

import net.dv8tion.jda.api.entities.TextChannel;


public class HelpCommand implements Commands {
	
	
	private final CommandManager manager; 
	
	public HelpCommand(CommandManager manager) {
		this.manager = manager;
	};

	@Override
	public void handle(CommandContext ctx) {
		List<String> args = ctx.getArgs();
		TextChannel channel = ctx.getChannel();
		
		if (args.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			
			builder.append("List of Commands\n");
			
			manager.getCommands().stream().map(Commands::getName).forEach((it) -> builder.append("`").append(Config.get("prefix")).append(it).append("`\n"));
			
			channel.sendMessage(builder.toString()).queue();
			
			return;
		}

		String search = args.get(0);
		Commands command = manager.getCommand(search);
		
		if (command == null) {
			channel.sendMessage("Hmmm, that command doesn't seem to be here").queue();
			return;
		}
		
		channel.sendMessage(command.getHelp()).queue();
	}


	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getHelp() {
		return "Shows the list with commands in the bot\n" + "Usage: !help <Command>";
	}

}
