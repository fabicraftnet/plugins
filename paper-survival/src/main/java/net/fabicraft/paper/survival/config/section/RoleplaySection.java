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

	@Comments("/me command radius in blocks")
	@Comments("Needs to be manually synced with the radius configured in Carbon")
	default int meRadius() {
		return 100;
	}

	@Comments("/me command format")
	@Comments("Available placeholders: <name> <action>")
	default String meFormat() {
		return "<italic><#94a3b8>* <name> <action>";
	}
}
