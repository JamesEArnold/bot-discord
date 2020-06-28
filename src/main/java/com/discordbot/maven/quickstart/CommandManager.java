package com.discordbot.maven.quickstart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.discordbot.maven.command.CommandContext;
import com.discordbot.maven.command.Commands;
import com.discordbot.maven.command.actions.DollarCheckerCommand;
import com.discordbot.maven.command.actions.DuelCommand;
import com.discordbot.maven.command.actions.GambleCommand;
import com.discordbot.maven.command.actions.HelpCommand;
import com.discordbot.maven.command.actions.PingCommand;
import com.discordbot.maven.music.JoinCommand;
import com.discordbot.maven.music.LeaveCommand;
import com.discordbot.maven.music.PlayCommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandManager {
	private final List<Commands> commands = new ArrayList<Commands>();
	EventWaiter waiter;
	
	
	public CommandManager(EventWaiter waiter) {
		// Inside of our constructor we build our commands list utilizing our addCommand methods.
		this.waiter = waiter;
		addCommand(new PingCommand());
		addCommand(new HelpCommand(this));
		addCommand(new DuelCommand(waiter));
		addCommand(new PlayCommand());
		addCommand(new JoinCommand());
		addCommand(new LeaveCommand());
		addCommand(new DollarCheckerCommand());
		addCommand(new GambleCommand());
	}

	private void addCommand(Commands cmd) {
		boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));
		
		if (nameFound) {
			throw new IllegalArgumentException("This command is already in the list!");
		}
		
		commands.add(cmd);
	}
	
	public Commands getCommand(String search) {
		String searchLower = search.toLowerCase();
		
		for(Commands cmd : this.commands) {
			if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
				return cmd;
			}
		}
		
		return null;
	}
	
	public List<Commands> getCommands() {
		return commands;
	}
	 
	void handle(GuildMessageReceivedEvent event) {
		
		// First we create our array split, which gathers the the content of the message.
		// Then we remove the prefix, and split it on white space
		String[] split = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(Config.get("prefix")), "").split("\\s+");
		
		String invoke = split[0].toLowerCase();
		
		Commands cmd = this.getCommand(invoke);
		
		if (cmd != null) {
			
			
			event.getChannel().sendTyping().queue();
			
			//
			List<String> args = Arrays.asList(split).subList(1, split.length);
			
			CommandContext ctx = new CommandContext(event, args);
			
			cmd.handle(ctx);
			ctx.messageCleanup();
		}
	}

}
