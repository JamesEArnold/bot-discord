package com.discordbot.maven.command;

import java.util.Arrays;
import java.util.List;

public interface Commands {
	
	void handle(CommandContext ctx);
	
	String getName();
	
	String getHelp();
	
	default List<String> getAliases() {
		return Arrays.asList();
	}

}
