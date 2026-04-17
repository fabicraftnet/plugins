package net.fabicraft.velocity.locale;

import net.fabicraft.common.locale.TranslationManager;
import net.kyori.adventure.key.Key;
import org.slf4j.Logger;

import java.util.List;
import java.util.Locale;

public final class VelocityTranslationManager extends TranslationManager {
	public VelocityTranslationManager(Logger logger) {
		super(logger);
	}

	@Override
	protected List<Locale> bundledLocales() {
		return List.of(Locale.ENGLISH, Locale.of("fi", "FI"));
	}

	@Override
	protected Key name() {
		return Key.key("fabicraft", "velocity");
	}
}
