package net.fabicraft.paper.survival.command;

import net.fabicraft.paper.survival.items.CustomItemManager;
import org.incendo.cloud.key.CloudKey;

public final class SurvivalCommandContextKeys {
	public static final CloudKey<CustomItemManager> CUSTOM_ITEM_MANAGER_KEY = CloudKey.of("CustomItemManager", CustomItemManager.class);

	private SurvivalCommandContextKeys() {
	}
}
