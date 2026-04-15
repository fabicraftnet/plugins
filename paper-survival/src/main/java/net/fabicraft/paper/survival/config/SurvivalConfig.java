package net.fabicraft.paper.survival.config;

import net.fabicraft.paper.survival.config.section.RoleplaySection;
import space.arim.dazzleconf.engine.liaison.SubSection;

import java.util.List;

public interface SurvivalConfig {
	@SubSection
	RoleplaySection roleplay();

	default List<String> firstJoinItems() {
		return List.of(
				"tonttilapio",
				"bread 16",
				"bamboo_raft"
		);
	}
}
