package net.fabicraft.paper.survival.config;

import net.fabicraft.paper.survival.config.section.RolePlaySection;
import space.arim.dazzleconf.engine.liaison.SubSection;

import java.util.List;

public interface SurvivalConfig {
	@SubSection
	RolePlaySection rolePlay();

	default List<String> firstJoinItems() {
		return List.of(
				"tonttilapio",
				"bread 16",
				"bamboo_raft"
		);
	}
}
