package net.fabicraft.paper.survival.command;

import net.fabicraft.paper.survival.ItemManager;
import net.fabicraft.paper.survival.gathering.GatheringManager;
import org.incendo.cloud.key.CloudKey;

public final class SurvivalCommandContextKeys {
	public static final CloudKey<ItemManager> CUSTOM_ITEM_MANAGER_KEY = CloudKey.of("CustomItemManager", ItemManager.class);
	public static final CloudKey<GatheringManager> GATHERING_MANAGER_KEY = CloudKey.of("GatheringManager", GatheringManager.class);

	private SurvivalCommandContextKeys() {
	}
}
