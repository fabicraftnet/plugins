package net.fabicraft.common.locale;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationStore;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class TranslationManager {
	private final Logger logger;
	private final TranslationStore.StringBased<MessageFormat> translationStore;

	public TranslationManager(Logger logger) {
		this.logger = logger;

		this.translationStore = TranslationStore.messageFormat(name());
		this.translationStore.defaultLocale(defaultLocale());

		loadFromResourceBundle();

		GlobalTranslator.translator().addSource(this.translationStore);
	}

	/**
	 * Loads the bundled translations from the jar file.
	 */
	private void loadFromResourceBundle() {
		try {
			bundledLocales().forEach(locale -> {
				ResourceBundle bundle = ResourceBundle.getBundle("messages", locale, getClass().getClassLoader());
				this.translationStore.registerAll(locale, bundle, false);
			});
		} catch (IllegalArgumentException e) {
			this.logger.warn("Error loading default locale file", e);
		}
	}

	protected Locale defaultLocale() {
		return Locale.of("fi", "FI");
	}

	protected abstract List<Locale> bundledLocales();

	protected abstract Key name();
}
