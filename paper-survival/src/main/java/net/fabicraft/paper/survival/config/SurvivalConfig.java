package net.fabicraft.paper.survival.config;

import net.fabicraft.paper.survival.config.section.RolePlaySection;
import space.arim.dazzleconf.engine.liaison.SubSection;

public interface SurvivalConfig {
	@SubSection
	RolePlaySection rolePlay();
}
