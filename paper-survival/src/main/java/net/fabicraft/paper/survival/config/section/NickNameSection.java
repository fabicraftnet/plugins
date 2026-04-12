package net.fabicraft.paper.survival.config.section;

public interface NickNameSection {
	default int minLength() {
		return 3;
	}

	default int maxLength() {
		return 20;
	}
}
