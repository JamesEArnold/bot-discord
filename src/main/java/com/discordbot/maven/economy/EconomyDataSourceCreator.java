package com.discordbot.maven.economy;

import org.apache.commons.dbcp2.BasicDataSource;

import com.discordbot.maven.discorduser.JDBCDiscordUserDAO;
import com.discordbot.maven.quickstart.Config;

public class EconomyDataSourceCreator {
	
	public static JDBCDiscordUserDAO main() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl(Config.get("databaseurl"));
		dataSource.setUsername(Config.get("databaseusername"));
		dataSource.setPassword(Config.get("databasepassword"));
		
		JDBCDiscordUserDAO discordDAO = new JDBCDiscordUserDAO(dataSource);
		
		return discordDAO;
	}
	

}
