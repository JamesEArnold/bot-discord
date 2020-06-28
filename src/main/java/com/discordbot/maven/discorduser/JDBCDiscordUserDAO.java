package com.discordbot.maven.discorduser;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import net.dv8tion.jda.api.entities.Member;

public class JDBCDiscordUserDAO implements DiscordUserDAO {
	
	private final JdbcTemplate jdbcTemplate;
	
	public JDBCDiscordUserDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Override
	public void createNewUser(Member user) {
		String sqlInsertUser = "INSERT INTO bot_economy (user_id, name, guild_id, dollars, voice_time) "
				+ "VALUES (?, ?, ?, 0, 0)";
		
		jdbcTemplate.update(sqlInsertUser, Long.parseLong(user.getId()), user.getEffectiveName(), Long.parseLong(user.getGuild().getId()));
		
	}
	
	@Override
	public boolean checkIfUserExists(Member user) {
		String sqlCheckForUser = "SELECT COUNT(*) from bot_economy WHERE user_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlCheckForUser, Long.parseLong(user.getId()));
		results.next();
		
		if (results.getInt("count") == 1) {
			return true;
		}
		return false;
	}
	
	@Override
	public void addDollarsForVoiceTime(Member user) {
		String sqlUpdateDollars = "UPDATE bot_economy SET dollars = (SELECT dollars)+ 5, voice_time = (SELECT voice_time) + 5 WHERE user_id = ?";
		jdbcTemplate.update(sqlUpdateDollars, Long.parseLong(user.getId()));
		
	}

	
	@Override
	public DiscordUser findUserByUserId(long id) {
		DiscordUser theUser = null;
		String sqlFindUserById = "SELECT name, dollars, voice_time FROM bot_economy WHERE user_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindUserById, id);
		
		if (results.next()) {
			theUser = mapRowToUser(results);
		}
		return theUser;
	}
	
	
	@Override
	public void removeDollarsFromUser(Member user, int amountOfDollars) {
		String sqlUpdateDollars = "UPDATE bot_economy SET dollars = (SELECT dollars) - ? WHERE user_id = ?";
		jdbcTemplate.update(sqlUpdateDollars, amountOfDollars, Long.parseLong(user.getId()));
	}
	
	
	@Override
	public void addDollarsToUser(Member user, int amountOfDollars) {
		String sqlUpdateDollars = "UPDATE bot_economy SET dollars = (SELECT dollars) + ? WHERE user_id = ?";
		jdbcTemplate.update(sqlUpdateDollars, amountOfDollars, Long.parseLong(user.getId()));
	}
	
	@Override
	public ArrayList<DiscordUser> dollarCheckByGuildId(String guildId) {
		String sqlCheckForDollars = "SELECT user_id, name, guild_id, dollars, voice_time FROM bot_economy WHERE guild_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlCheckForDollars, Long.parseLong(guildId));
		
		ArrayList<DiscordUser> users = new ArrayList<DiscordUser>();
		DiscordUser theUser = null;
		
		while (results.next()) {
			theUser = mapRowToUser(results);
			users.add(theUser);
		}
		
		return users;
	}
	
	@Override
	public int checkFundsForSpecificUser(Member user) {
		String sqlFindUserById = "SELECT user_id, name, guild_id, dollars, voice_time FROM bot_economy WHERE user_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindUserById, Long.parseLong(user.getId()));
		
		if (results.next()) {
			return results.getInt("dollars");
		}
		
		return 0;
		
	}
	
	public void jackpotIncreaseForTime() {
		String sqlUpdateDollars = "UPDATE bot_economy SET dollars = (SELECT dollars)+ 5, voice_time = (SELECT voice_time) + 5 WHERE user_id = ?";
		jdbcTemplate.update(sqlUpdateDollars, Long.parseLong("1"));
	}
	
	private DiscordUser mapRowToUser(SqlRowSet results) {
		// We cannot instantiate new Member objects.  
		// Because of this we'll create an identical object we can instantiate
		DiscordUser theUser;
		theUser = new DiscordUser();
		
		theUser.setUserId(results.getLong("user_id"));
		theUser.setName(results.getString("name"));
		theUser.setDollars(results.getInt("dollars"));
		theUser.setVoiceTime(results.getInt("voice_time"));
		
		return theUser;
	}
	
	
	

}
