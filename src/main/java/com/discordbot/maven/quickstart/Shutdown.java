package com.discordbot.maven.quickstart;

import net.dv8tion.jda.api.JDA;

public class Shutdown {

	public static void shutdown(JDA jda) {
		jda.getHttpClient().connectionPool().evictAll();
		jda.getHttpClient().dispatcher().executorService().shutdown();
	}
}
