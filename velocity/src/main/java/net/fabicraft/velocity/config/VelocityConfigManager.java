package net.fabicraft.velocity.config;

import net.fabicraft.velocity.FabiCraftVelocity;
import net.fabicraft.velocity.config.liaison.ProtocolVersionLiaison;
import org.slf4j.Logger;
import space.arim.dazzleconf.Configuration;
import space.arim.dazzleconf.StandardErrorPrint;
import space.arim.dazzleconf.backend.Backend;
import space.arim.dazzleconf.backend.PathRoot;
import space.arim.dazzleconf.backend.toml.TomlBackend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class VelocityConfigManager {
	private final Configuration<VelocityConfig> configuration;
	private final Backend backend;
	private final Path dataDirectory;
	private final Logger logger;
	private final StandardErrorPrint errorPrint;
	private VelocityConfig config;

	public VelocityConfigManager(FabiCraftVelocity plugin) {
		this.dataDirectory = plugin.dataDirectory();
		this.logger = plugin.logger();

		this.configuration = Configuration.defaultBuilder(VelocityConfig.class).addTypeLiaisons(new ProtocolVersionLiaison()).build();
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

	public VelocityConfig config() {
		return this.config;
	}
}
