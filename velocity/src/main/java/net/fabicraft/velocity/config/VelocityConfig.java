package net.fabicraft.velocity.config;

import net.fabicraft.velocity.config.section.PingSection;
import net.fabicraft.velocity.config.section.VersionSection;
import space.arim.dazzleconf.engine.liaison.SubSection;

public interface VelocityConfig {
	@SubSection
	VersionSection version();

	@SubSection
	PingSection ping();
}
