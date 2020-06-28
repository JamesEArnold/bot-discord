package com.discordbot.maven.music;

import java.util.List;

import com.discordbot.maven.command.CommandContext;
import com.discordbot.maven.command.Commands;

public class PlayCommand implements Commands {

	@Override
	public void handle(CommandContext ctx) {
		PlayerManager manager = PlayerManager.getInstance();
		
		// Grab the song name to make sure it's present
		final List<String> args = ctx.getArgs();
				
		// Making sure the song name is there
		if (args.size() < 1 ) {
			ctx.easyMessageSend("You seem to be missing some inputs.  Did you miss something? !help for more information.");
			return;
		}
		
		String songKeyWords = ctx.argParser();
		
		manager.loadAndPlay(ctx.getEvent().getChannel(), songKeyWords);
		
		manager.getGuildMusicManager(ctx.getEvent().getGuild()).player.setVolume(50);
		
	}

	@Override
	public String getName() {
		return "play";
	}

	@Override
	public String getHelp() {
		return "Use the command !play <SONG NAME> to play a song";
	}
	

}
