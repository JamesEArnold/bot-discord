package com.discordbot.maven.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.sharding.ShardManager;

/**
 * 
 * This interface will help us access our events particular context
 *
 */

public interface EventContext {
	
	/*
	 * Return this specific events {@link net.dv8tion.jda.api.entities.Guild}
	 */
	
	Guild getGuild();
	
	
	/*
	 * Returns the specific event that instantiated this instance - {@link net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent}
	 */
	
	GuildMessageReceivedEvent getEvent();
	
	/*
	 * Default method so that children are not required to implement/override the method
	 * Returns the message {@link net.dv8tion.jda.api.entities.Message message;} that triggered this particular event
	 */
	default Message getMessage() {
		return this.getEvent().getMessage();
	}
	
	/*
	 * Default method so that children are not required to implement/override the method
	 * Returns the author {@link net.dv8tion.jda.api.entities.User;} that triggered this particular event as a User
	 */
	
	default User getAuthor() {
		return this.getEvent().getAuthor();
	}
	
	/*
	 * Default method so that children are not required to implement/override the method
	 * Returns the channel {@link net.dv8tion.jda.api.entities.TextChannel;} the event was sent in
	 */
	
	default TextChannel getChannel() {
		return this.getEvent().getChannel();
	}
	
	/*
	 * Default method so that children are not required to implement/override the method
	 * Returns the author {@link net.dv8tion.jda.api.entities.Member;} the triggered this particular event as a Member
	 */
	
	default Member getMember() {
		return this.getEvent().getMember();
	}
	
	/*
	 * Default method so that children are not required to implement/override the method
	 * Returns the current instantiated JDA {@link net.dv8tion.jda.api.JDA}
	 */
	
	default JDA getJDA() {
		return this.getEvent().getJDA();
	}
	
	/*
	 * Default method so that children are not required to implement/override the method
	 * The Shard Manager is for very large bots with over 1000 Guilds.  Not Likely Needed.
	 * Returns the {@link net.dv8tion.jda.api.sharding.ShardManager}
	 */
	
	default ShardManager getShardManager() {
		return this.getJDA().getShardManager();
	}
	
	/*
	 * Default method so that children are not required to implement/override the method
	 * Returns the currently logged in account represented by SelfUser.
	 * {@link net.dv8tion.jda.api.entities.User}
	 */
	
	default User getSelfUser() {
		return this.getJDA().getSelfUser();
	}
	
	/*
	 * Default method so that children are not required to implement/override the method
	 * Returns the currently logged in account represented by SelfMember. {@link net.dv8tion.jda.api.entities.Member}
	 */
	
	default Member getSelfMember() {
		return this.getGuild().getSelfMember();
	}
	
	
	

}
