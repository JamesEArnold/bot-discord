package com.discordbot.maven.music;

import com.discordbot.maven.command.CommandContext;
import com.discordbot.maven.command.Commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand implements Commands {

	@Override
	public void handle(CommandContext ctx) {
		AudioManager audioManager = ctx.getEvent().getGuild().getAudioManager();
		
		
		// See if the bots audioManager is already in a voice channel
		if (audioManager.isConnected()) {
			ctx.easyMessageSend("Hmmm, I'm already connected to a channel");
			return;
		}
		
		// Get the users voice state
		GuildVoiceState memberVoiceState = ctx.getEvent().getMember().getVoiceState();
		// Let the user know they're not in a voice channel
		if (!memberVoiceState.inVoiceChannel()) {
			ctx.easyMessageSend("It doesn't look like you're in a voice channel");
		}
		
		VoiceChannel voiceChannel = memberVoiceState.getChannel();
		Member selfMember = ctx.getEvent().getGuild().getSelfMember();
		
		// Next we'll check to see if it's possible for our bot to connect to the voice channel
		if (!selfMember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
			ctx.easyMessageSend("I don't have permissions to join that voice channel");
			return;
		}
		
		audioManager.openAudioConnection(voiceChannel);
		ctx.easyMessageSend("Joining your voice channel...");
	}

	@Override
	public String getName() {
		return "join";
	}

	@Override
	public String getHelp() {
		return "The bot will join the user's current voice channel";
	}

}
