package net.fabicraft.paper.locale;

import net.kyori.adventure.key.Key;
import org.slf4j.Logger;

import java.util.List;
import java.util.Locale;

public final class PaperTranslationManager extends net.fabicraft.common.locale.TranslationManager {
	public PaperTranslationManager(Logger logger) {
		super(logger);
	}

	@Override
	protected List<Locale> bundledLocales() {
		return List.of(Locale.ENGLISH, Locale.of("fi", "FI"));
	}

	@Override
	protected Key name() {
		return Key.key("fabicraft", "paper");
	}
}
