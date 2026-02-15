package net.fabicraft.paper.survival.config;

import space.arim.dazzleconf.engine.Comments;

public interface SurvivalConfig {
	@Comments("Idle time in seconds before a player is marked as AFK")
	default int afkMarkSeconds() {
		return 300;
	}

	@Comments("Total idle time in seconds before a player is kicked")
	default int afkKickSeconds() {
		return 600;
	}

	@Comments("Seconds before the kick when a warning is sent")
	default int afkWarnBeforeKickSeconds() {
		return 30;
	}

}
