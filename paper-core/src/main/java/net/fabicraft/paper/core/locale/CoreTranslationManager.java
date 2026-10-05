package net.fabicraft.paper.core.locale;

import net.fabicraft.common.locale.TranslationManager;
import net.kyori.adventure.key.Key;
import org.slf4j.Logger;


public final class CoreTranslationManager extends TranslationManager {
	public CoreTranslationManager(Logger logger) {
		super(logger);
	}

	@Override
	protected Key name() {
		return Key.key("fabicraft", "core");
	}
}
