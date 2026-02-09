package net.fabicraft.paper.survival.config;

public interface SurvivalConfig {
	default int afkTimeoutSeconds() {
		return 300;
	}
}
