package net.fabicraft.paper.survival.npc;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.gui.PlayerArenaListGui;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

@TraitName("fabicraftplayerarenalist")
public final class PlayerArenaListTrait extends Trait {
	private final FabiCraftPaperSurvival plugin;

	public PlayerArenaListTrait() {
		super("fabicraftplayerarenalist");
		this.plugin = JavaPlugin.getPlugin(FabiCraftPaperSurvival.class);
	}

	@EventHandler
	public void click(NPCRightClickEvent event) {
		if (event.getNPC() != this.getNPC()) {
			return;
		}

		new PlayerArenaListGui(this.plugin).open(event.getClicker());
	}
}
