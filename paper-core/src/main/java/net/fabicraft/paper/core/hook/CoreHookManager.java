package net.fabicraft.paper.core.hook;

import net.fabicraft.paper.core.FabiCraftPaperCore;

public final class CoreHookManager extends HookManager<FabiCraftPaperCore> {
	private HuskHomesHook huskHomesHook;

	public CoreHookManager(FabiCraftPaperCore plugin) {
		super(plugin);
	}

	@Override
	public void register() {
		this.huskHomesHook = register("HuskHomes", () -> new HuskHomesHook(super.plugin));
		logSummary();
	}

	public HuskHomesHook huskHomesHook() {
		return this.huskHomesHook;
	}
}
