package net.fabicraft.paper.bedwars;

import de.marcely.bedwars.api.GameAPI;
import net.fabicraft.paper.bedwars.command.SpawnCommand;
import net.fabicraft.paper.bedwars.listener.EntityListener;
import net.fabicraft.paper.bedwars.locale.BedwarsTranslationManager;
import net.fabicraft.paper.bedwars.shop.FabiCraftShopLayout;
import net.fabicraft.paper.core.FabiCraftPaperCore;
import net.fabicraft.paper.core.FabiCraftPaperPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;

import java.util.List;

public final class FabiCraftPaperBedwars extends JavaPlugin implements FabiCraftPaperPlugin {
	private GameAPI api;
	private PaperCommandManager<Source> commandManager;
	private FabiCraftPaperCore core;

	public FabiCraftPaperBedwars() {
		new BedwarsTranslationManager(getSLF4JLogger());
	}

	@Override
	public void onEnable() {
		this.core = getPlugin(FabiCraftPaperCore.class);

		this.api = GameAPI.get();
		this.api.registerShopLayout(new FabiCraftShopLayout(this));
		registerCommands();
		registerListeners();
	}

	public GameAPI api() {
		return this.api;
	}

	private void registerCommands() {
		List.of(
				new SpawnCommand(this)
		).forEach(command -> command.register(this.core.commandManager()));
	}

	private void registerListeners() {
		PluginManager manager = getServer().getPluginManager();
		List.of(
				new EntityListener()
		).forEach(listener -> manager.registerEvents(listener, this));
	}

	@Override
	public void load() {

	}

	@Override
	public String identifier() {
		return "bedwars";
	}
}
