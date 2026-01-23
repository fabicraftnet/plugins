package net.fabicraft.survival;

import net.fabicraft.paper.FabiCraftPaper;
import net.fabicraft.paper.luckperms.PaperLuckPermsManager;
import net.fabicraft.survival.command.PaperSurvivalCommand;
import net.fabicraft.survival.command.commands.RolePlayCommand;
import net.fabicraft.survival.command.commands.SetFirstSpawnCommand;
import net.fabicraft.survival.listener.PlayerListener;
import net.fabicraft.survival.locale.SurvivalTranslationManager;
import net.fabicraft.survival.spawn.FirstSpawnManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.List;

public final class FabiCraftSurvival extends JavaPlugin {
	private FabiCraftPaper fabiCraftPaper;
	private FirstSpawnManager firstSpawnManager;

	@Override
	public void onEnable() {
		this.fabiCraftPaper = getPlugin(FabiCraftPaper.class);

		new SurvivalTranslationManager(getSLF4JLogger());

		this.firstSpawnManager = new FirstSpawnManager(this);
		this.firstSpawnManager.load();

		registerCommands();
		registerListeners();
	}

	public FirstSpawnManager firstSpawnManager() {
		return this.firstSpawnManager;
	}

	public PaperCommandManager<Source> commandManager() {
		return this.fabiCraftPaper.commandManager();
	}

	public PaperLuckPermsManager luckPermsManager() {
		return this.fabiCraftPaper.luckPermsManager();
	}

	private void registerListeners() {
		PluginManager manager = getServer().getPluginManager();
		List.of(
				new PlayerListener(this)
		).forEach(listener -> manager.registerEvents(listener, this));
	}

	private void registerCommands() {
		List.of(
				new SetFirstSpawnCommand(this),
				new RolePlayCommand(this)
		).forEach(PaperSurvivalCommand::register);
	}

	@Override
	public @NonNull Path getDataPath() {
		return this.fabiCraftPaper.getDataPath().resolve("survival");
	}
}
