package net.fabicraft.paper.survival.hook;

import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.hook.miniplaceholders.MiniPlaceholdersHook;
import org.bukkit.plugin.PluginManager;

public final class HookManager {
	private final PluginManager pluginManager;
	private final FabiCraftPaperSurvival plugin;
	private int registered = 0;

	public HookManager(FabiCraftPaperSurvival plugin) {
		this.plugin = plugin;
		this.pluginManager = plugin.getServer().getPluginManager();
	}

	public void register() {
		if (this.pluginManager.isPluginEnabled("MiniPlaceholders")) {
			register(new MiniPlaceholdersHook(this.plugin));
		}
		if (this.pluginManager.isPluginEnabled("Citizens")) {
			register(new CitizensHook());
		}
		this.plugin.getSLF4JLogger().info("Total of {} hooks registered", this.registered);
	}

	private void register(Hook hook) {
		hook.register();
		this.registered++;
	}
}
