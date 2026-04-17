package net.fabicraft.velocity;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.fabicraft.common.command.ExceptionHandler;
import net.fabicraft.common.command.MinecraftCaptionProvider;
import net.fabicraft.common.locale.BrandColor;
import net.fabicraft.velocity.command.VelocityCommand;
import net.fabicraft.velocity.command.commands.FabiCraftVelocityCommand;
import net.fabicraft.velocity.config.VelocityConfig;
import net.fabicraft.velocity.config.VelocityConfigManager;
import net.fabicraft.velocity.listener.LoginListener;
import net.fabicraft.velocity.listener.PingListener;
import net.fabicraft.velocity.locale.VelocityTranslationManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.velocity.CloudInjectionModule;
import org.incendo.cloud.velocity.VelocityCommandManager;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

public final class FabiCraftVelocity {
	private final MiniMessage.Builder miniMessageBuilder = MiniMessage.builder()
			.tags(TagResolver.resolver(
					StandardTags.defaults(),
					BrandColor.resolver()
			));
	private final MiniMessage miniMessage = this.miniMessageBuilder.build();
	private final ProxyServer server;
	private final Logger logger;
	private final Path dataDirectory;
	private final VelocityConfigManager configManager;
	private final Injector injector;
	private final IconManager iconManager;
	private final ExecutorService executorService;
	private VelocityCommandManager<CommandSource> commandManager;

	@Inject
	public FabiCraftVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory, Injector injector, ExecutorService executorService) {
		this.server = server;
		this.logger = logger;
		this.dataDirectory = dataDirectory;
		this.injector = injector;
		this.executorService = executorService;

		new VelocityTranslationManager(logger);

		this.configManager = new VelocityConfigManager(this);
		this.iconManager = new IconManager(this);
	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		load();

		setupCommandManager();
		registerCommands();

		registerListeners();
	}

	public void load() {
		this.configManager.load();
		this.iconManager.load();
	}

	public VelocityCommandManager<CommandSource> commandManager() {
		return this.commandManager;
	}

	public VelocityConfig config() {
		return this.configManager.config();
	}

	public Path dataDirectory() {
		return this.dataDirectory;
	}

	public ProxyServer server() {
		return this.server;
	}

	public Logger logger() {
		return this.logger;
	}

	public IconManager iconManager() {
		return this.iconManager;
	}

	public MiniMessage.Builder miniMessageBuilder() {
		return this.miniMessageBuilder;
	}

	public MiniMessage miniMessage() {
		return this.miniMessage;
	}

	private void setupCommandManager() {
		Injector childInjector = this.injector.createChildInjector(
				new CloudInjectionModule<>(
						CommandSource.class,
						ExecutionCoordinator.simpleCoordinator(),
						SenderMapper.identity()
				)
		);

		VelocityCommandManager<CommandSource> commandManager = childInjector.getInstance(
				com.google.inject.Key.get(new TypeLiteral<>() {
				})
		);

		// Custom error handling
		commandManager.captionRegistry().registerProvider(new MinecraftCaptionProvider<>());
		new ExceptionHandler<CommandSource>(this.logger, source -> source).register(commandManager);

		this.commandManager = commandManager;
	}

	private void registerCommands() {
		Stream.of(
				new FabiCraftVelocityCommand(this)
		).forEach(VelocityCommand::register);
	}

	private void registerListeners() {
		EventManager manager = this.server.getEventManager();
		Stream.of(
				new LoginListener(),
				new PingListener(this)
		).forEach(listener -> manager.register(this, listener));
	}
}
