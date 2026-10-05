package net.fabicraft.common.config;

import org.slf4j.Logger;
import space.arim.dazzleconf.Configuration;
import space.arim.dazzleconf.StandardErrorPrint;
import space.arim.dazzleconf.backend.Backend;
import space.arim.dazzleconf.backend.PathRoot;
import space.arim.dazzleconf.backend.toml.TomlBackend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager<T> {
	private final Configuration<T> configuration;
	private final Backend backend;
	private final StandardErrorPrint errorPrint;
	private final Path directory;
	private final Logger logger;
	private volatile T config;

	public ConfigManager(Class<T> type, Path directory, String fileName, Logger logger) {
		this.directory = directory;
		this.logger = logger;

		this.configuration = Configuration.defaultBuilder(type).build();
		this.backend = new TomlBackend(new PathRoot(directory.resolve(fileName)));
		this.errorPrint = new StandardErrorPrint(output -> this.logger.error(output.printString()));
	}

	public void load() {
		try {
			Files.createDirectories(this.directory);
		} catch (IOException e) {
			this.logger.error("Failed to create directory {}", this.directory, e);
		}
		this.config = this.configuration.configureOrFallback(this.backend, this.errorPrint);
	}

	public T config() {
		return this.config;
	}
}
