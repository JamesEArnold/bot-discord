package com.discordbot.maven.music;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class PlayerManager {
	// We create this instance status to protect ourselves from multiple threads in Lava player
	private static PlayerManager INSTANCE;
	
	private final AudioPlayerManager playerManager;
	private final Map<Long, GuildMusicManager> musicManagers;
	
	private PlayerManager() {
		// We initiate the default player manager from LavaPlayer
		this.playerManager = new DefaultAudioPlayerManager();
		// We enable this so we can access remote sources for music
		AudioSourceManagers.registerRemoteSources(playerManager);
		// We enable this so we can access local sources for music
		AudioSourceManagers.registerLocalSource(playerManager);
		
		this.musicManagers = new HashMap<>();
	}
	
	public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
		long guildId = guild.getIdLong();
		
		// We get our music manager from the map we made
		GuildMusicManager musicManager = musicManagers.get(guildId);
		
		// If that music manager is null we're going to create it
		if (musicManager == null) {
			// We create our music manager to be played into the hashmap
			musicManager = new GuildMusicManager(playerManager);
			// We play our guildId as the key, and our newly created music manager as the value
			musicManagers.put(guildId, musicManager);
		}
		
		// Here we set the sendhandler onto the audio manager in this case a new audio player send handler
		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
		
		return musicManager;
	}
	
	public void loadAndPlay(TextChannel channel, String songUrl) {
		GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());
		
		String songUrlYT = "ytsearch: " + songUrl;
		
		playerManager.loadItemOrdered(musicManager, songUrlYT, new AudioLoadResultHandler() {

			// If our song is found in a single YouTube video, this method will be called
			@Override
			public void trackLoaded(AudioTrack track) {
				channel.sendMessage("Adding your song to the queue: " + track.getInfo().title).queue(message -> message.delete().queueAfter(20, TimeUnit.SECONDS));
				
				// Calling our music manager and passing in our found audio track to be played
				play(musicManager, track);
				
			}

			// If our song is found in a YouTube playlist, this method will be called
			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				AudioTrack firstTrack = playlist.getSelectedTrack();
				
				if (firstTrack == null) {
					firstTrack = playlist.getTracks().get(0);
				}
				
				channel.sendMessage("Adding to the queue: " + firstTrack.getInfo().title).queue(message -> message.delete().queueAfter(20, TimeUnit.SECONDS));
				
				// Calling our music manager and passing in our found audio track to be played
				play(musicManager, firstTrack);
				
			}

			@Override
			public void noMatches() {
				channel.sendMessage("Nothing found by: " + songUrl).queue(message -> message.delete().queueAfter(20, TimeUnit.SECONDS));
				
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				channel.sendMessage("Could not play: " + exception.getMessage()).queue(message -> message.delete().queueAfter(20, TimeUnit.SECONDS));
				
			}
			
		});
		
		
	}
	
	private void play(GuildMusicManager musicManager, AudioTrack track) {
		// This grabs the musicManager, gets the scheduler and queues the track
		musicManager.scheduler.queue(track);
	}

	public static synchronized PlayerManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PlayerManager();
		}
		return INSTANCE;
	}
}

