package com.discordbot.maven.quickstart;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.discordbot.maven.economy.EconomyAddDollars;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;



public class Listener extends ListenerAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
	private final CommandManager manager;
	public ArrayList<Member> activeVoiceMembers = new ArrayList<Member>();
	private Timer timer = new Timer();
	
	
	public Listener(CommandManager manager) {
		this.manager = manager;
	}


	@Override
	public void onReady(ReadyEvent event) {
		LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
		
		// OnReady we'll compile a list of our Discord Users who are 
		// currently in our voice channel
		ArrayList<VoiceChannel> voiceChannels = new ArrayList<VoiceChannel>(event.getJDA().getVoiceChannels());
		
		// We'll iterate through our list of voiceChannels to find our one with users actively in it
		// This method will assign our active users to our activeVoiceMembers list
		activeMembers(voiceChannels);
		
		// We need to send our activeMembers to our running economy function
		timer.schedule(new EconomyAddDollars(activeVoiceMembers), 0, 300000);
	}

	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		

		User user = event.getAuthor();

		// Make sure the bot isn't talking to itself.  Webhook messages don't have a member ID
		// so they will likely crash something.
		if (user.isBot() || event.isWebhookMessage()) {
			return;
		}
		
		
		// Retrieving our prefix from our .env
		String prefix = Config.get("prefix");
		// Getting the raw string from the message passed
		String raw = event.getMessage().getContentRaw();
		
		// We'll evaluate the message contents to make sure they
		// are !shutdown && that the message author is equal to
		// the bot owner.
		if (raw.equalsIgnoreCase(prefix + "shutdown") && event.getAuthor().getId().equals(Config.get("owner_id"))) {
			
			LOGGER.info("Shutting Down");
			// We call our shutdown class, passing our JDA object
			// for it to shutdown gracefully.
			Shutdown.shutdown(event.getJDA());
			
			return;
		}
		
		if (raw.startsWith(prefix)) {
			manager.handle(event);
		}

	}
	
	// With this method we'll add joining users to our list of active
	// users in the voice channel
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) { 
		User user = event.getMember().getUser();
	
		if (user.isBot()) {
			return;
		}
		
		Member member = event.getMember();
		try {
			activeVoiceMembers.add(member);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
	}
	
	// With this method we'll remove the leaving user from our list of currently active
	// discord users.  This way, they're not gaining dollars while not present in the
	// voice channel
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		
		User user = event.getMember().getUser();
		
		if (user.isBot()) {
			return;
		}
	
		Member member = event.getMember();
		try {
			activeVoiceMembers.remove(member);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void activeMembers(List<VoiceChannel> voiceChannels) {
		
		// We'll utilize this private method to assign our active voice channel members on bot start up
		for (VoiceChannel voiceChannel : voiceChannels) {
			if (voiceChannel.getMembers().size() > 0) {
					activeVoiceMembers.addAll(new ArrayList<Member>(voiceChannel.getMembers()));
			}
		}
	}
	
}
