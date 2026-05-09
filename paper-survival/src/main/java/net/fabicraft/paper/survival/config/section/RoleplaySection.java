package net.fabicraft.paper.survival.config.section;

import space.arim.dazzleconf.engine.Comments;

public interface RoleplaySection {
	@Comments("Minimum allowed character name length")
	default int minNameLength() {
		return 3;
	}

	@Comments("Maximum allowed character name length")
	default int maxNameLength() {
		return 20;
	}

	@Comments("Minimum allowed character height in cm")
	default int minHeight() {
		return 150;
	}

	@Comments("Maximum allowed character height in cm")
	default int maxHeight() {
		return 200;
	}
}
