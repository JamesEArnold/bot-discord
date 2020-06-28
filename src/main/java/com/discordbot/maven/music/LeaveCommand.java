package com.discordbot.maven.music;

import com.discordbot.maven.command.CommandContext;
import com.discordbot.maven.command.Commands;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class LeaveCommand implements Commands {

	@Override
	public void handle(CommandContext ctx) {
		AudioManager audioManager = ctx.getEvent().getGuild().getAudioManager();
		
		if (!audioManager.isConnected()) {
			ctx.easyMessageSend("I'm not connected to a voice channel");
			return;
		}
		
		VoiceChannel voiceChannel = audioManager.getConnectedChannel();
		
		if (!voiceChannel.getMembers().contains(ctx.getEvent().getMember())) {
			ctx.easyMessageSend("You must be in the voice channel to make me leave");
			return;
		}
		
		audioManager.closeAudioConnection();
		ctx.easyMessageSend("I've left the voice channel");
		
	}

	@Override
	public String getName() {
		return "leave";
	}

	@Override
	public String getHelp() {
		return "This command will make the bot leave the voice channel";
	}
	
	

}
