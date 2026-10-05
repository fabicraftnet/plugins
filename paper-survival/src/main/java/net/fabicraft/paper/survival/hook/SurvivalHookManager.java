package net.fabicraft.paper.survival.hook;

import net.fabicraft.paper.core.hook.HookManager;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.hook.miniplaceholders.MiniPlaceholdersHook;

public final class SurvivalHookManager extends HookManager<FabiCraftPaperSurvival> {
	public SurvivalHookManager(FabiCraftPaperSurvival plugin) {
		super(plugin);
	}

	@Override
	public void register() {
		register("MiniPlaceholders", () -> new MiniPlaceholdersHook(super.plugin));
		register("Citizens", CitizensHook::new);
		logSummary();
	}
}
