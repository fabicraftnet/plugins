package net.fabicraft.paper.survival.config;

import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import org.slf4j.Logger;
import space.arim.dazzleconf.Configuration;
import space.arim.dazzleconf.StandardErrorPrint;
import space.arim.dazzleconf.backend.Backend;
import space.arim.dazzleconf.backend.PathRoot;
import space.arim.dazzleconf.backend.toml.TomlBackend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {
	private final Configuration<SurvivalConfig> configuration;
	private final Backend backend;
	private final StandardErrorPrint errorPrint;
	private final Path dataDirectory;
	private final Logger logger;
	private SurvivalConfig config;

	public ConfigManager(FabiCraftPaperSurvival plugin) {
		this.dataDirectory = plugin.getDataPath();
		this.logger = plugin.getSLF4JLogger();

		this.configuration = Configuration.defaultBuilder(SurvivalConfig.class).build();
		this.backend = new TomlBackend(new PathRoot(this.dataDirectory.resolve("config.toml")));
		this.errorPrint = new StandardErrorPrint(output -> this.logger.error(output.printString()));
	}

	public void load() {
		try {
			Files.createDirectories(this.dataDirectory);
		} catch (IOException e) {
			this.logger.error("Failed to create dataDirectory", e);
		}
		this.config = this.configuration.configureOrFallback(this.backend, this.errorPrint);
	}

	public SurvivalConfig config() {
		return this.config;
	}
}
