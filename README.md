# JDA Based Discord Bot

In my spare time I've had a blast building a Discord bot for my server of friends.  It's built in Java, utilizing JDA, a full wrapper of the Discord REST api.  It's been a great way to challenge myself while building something I can enjoy at the same time.

## Example of Features

![](BotExample300.gif)

## Features

### Message/Command Clean Up
Using a bot in your Discord server creates an amount of messages that interferes with actual discussion or posts.  Soon, it becomes just a wall of commands that really are not needed hours later.  I was able to have the Bot not only clean up it's own response messages but also commands issued by users after a 1 minute delay.  This allows the server to remain an actual place of discussion while still allowing users time to read Bot responses.

### Server Economy
The 'economy' for the Bot came about after several conversations on how I could make mini-games more interesting.  Utilizing SprintBoot JDBC, the Bot connects to a Postgresql database to store user information and statistics.  Upon Bot startup, the bot scans the voice channels of the guild to see what users are currently active within them.  These users are added to an active voice list and are awarded dollars based off of their up-time within the channel.  If a user leaves a channel, a 'GuildVoiceLeaveEvent' is registered with the bot, which will remove the user that triggered the event from the active voice list. Likewise when joining a channel, a 'GuildVoiceJoinEvent' is registered, adding the user to the active voice list where they will begin to receive dollars based on their uptime. I initially struggled with the registration of the join/leave events as well as methods that run on a timed delay.  This was my first opportunity to work with the java.util.Timer - adding dollars to users on the active voice list every 5 minutes.  When I saw it run successfully for the first time, it was definitely a high five moment.

### Duel between two users
The Duel mini-game that I built into the bot was quite a challenge for me. I had to first process the original duel request from User A - this kicks off the command.  The duel request includes an opponent (User B) argument. The challenge here was retaining User A information, while also correctly identifying User B and their acceptance of the duel.  The bot will actively deny other users who try to accept the duel, who are not User B.  This was also my first venture into what is referred to as an 'EventWaiter' in the JDA wrapper. After User A initial command, the EventWaiter is launched, readily waiting for a defined period of time for User B acceptance.  Upon acceptance, the bot randomly generates a winning letter and begins a countdown from 3 to 1. There are random time intervals between each countdown, so the users are not prepared for the randomly generated letter to be posted. Once the randomly generated letter is posted, another EventWaiter is launched waiting for User A or B to respond.  The EventWaiter then processes the incoming messages, determining if User A or B have responded with the correct letter, and if so, whoever sent the message first will be identified as the winner of the duel.

### Streaming Music
The Bot has music playback functionality that's really a hit in the server.  This functionality is built using LavaPlayer - an audio player which loads tracks from various sources and converts them into a stream of Opus frames. It's really a fantastic library to work with that provides great documentation.  First, a user initiates a !join command for the Bot to join the voice channel that user is currently in.  From there, the Bot can receive a !play <song name> command from any user within the voice channel.  The Bot has been configured to perform a YouTube search for the song requested, and begin almost immediately streaming the song into the voice channel for all users to hear.  The most common format used in YouTube is Opus, which matches the output format required for Discord.  This makes the packets from YouTube able to be directly passed to output, which saves CPU processing power. A queue has been built into the Bot, which allows for multiple users to add songs while one is currently playing.  When the current song is finished, the bot will move through the queue playing the next song automatically.  LavaPlayer is a very robust library that was challenging to manage.  It provided me with a great experience of having to read through the documentation to really try to understand what's going on behind the scenes and to implement it's functionality into my own Bot.  Again, another high five moment when I got to hear that first song query succeed and audio coming through my own bot.

### Gambling
Once the server economy was introduced I was able to expand on mini games to include lottery style games as well. I'm hoping to expand upon this in the coming weeks, but for now there is one lottery game implemented.  The user issues the !gamble <dollar amount> command and the user has a 5% chance of winning a 12x return of their bet.  I utilized a random number being generated, and if that random number falls within the defined range, the user wins the lottery.
