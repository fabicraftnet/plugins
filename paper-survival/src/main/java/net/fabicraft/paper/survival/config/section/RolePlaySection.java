package net.fabicraft.paper.survival.config.section;

import space.arim.dazzleconf.engine.Comments;

public interface RolePlaySection {
	default int minNameLength() {
		return 3;
	}

	default int maxNameLength() {
		return 20;
	}

	@Comments("Minimum allowed player height in cm")
	default int minHeight() {
		return 150;
	}

	@Comments("Maximum allowed player height in cm")
	default int maxHeight() {
		return 200;
	}
}
