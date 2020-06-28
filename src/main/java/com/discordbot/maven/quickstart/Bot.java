package com.discordbot.maven.quickstart;


import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.JDABuilder;

public class Bot {

    private Bot(String token) throws LoginException {
    	
    	EventWaiter waiter = new EventWaiter();
    	CommandManager commandManager = new CommandManager(waiter);

    	// This creates our connection to the Discord API by building
    	// a JDABuilder instance
    	// We pass our bot token to our createDefault constructor
    	// We construct a new Listener() object while calling the addEventListeners method
    	// Finally we build() our bot instance
    	JDABuilder.createDefault(token).addEventListeners(waiter, new Listener(commandManager)).build();

    }

    public static void main(String[] args) throws LoginException {
    	// Passing our Bot token to the Bot instance
    	// Using our Config class to retrieve our token from our .env
        new Bot(Config.get("token"));
    }

}