package net.fabicraft.paper.core.config;

import net.fabicraft.paper.core.FabiCraftPaperCore;
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
	private final Configuration<CoreConfig> configuration;
	private final Backend backend;
	private final StandardErrorPrint errorPrint;
	private final Path dataDirectory;
	private final Logger logger;
	private CoreConfig config;

	public ConfigManager(FabiCraftPaperCore plugin) {
		this.dataDirectory = plugin.getDataPath();
		this.logger = plugin.getSLF4JLogger();

		this.configuration = Configuration.defaultBuilder(CoreConfig.class).build();
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

	public CoreConfig config() {
		return this.config;
	}
}
