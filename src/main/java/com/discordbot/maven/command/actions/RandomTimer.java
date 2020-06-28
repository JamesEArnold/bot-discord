package com.discordbot.maven.command.actions;

import java.util.concurrent.TimeUnit;

public class RandomTimer {
	
	public void main() {
		try {
			TimeUnit.SECONDS.sleep((long) (Math.random() * 7));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
