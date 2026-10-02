package net.fabicraft.paper.survival.npc;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.dialog.RoleplayDialog;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

@TraitName("fabicraftroleplay")
public final class RoleplayTrait extends Trait {
	private final FabiCraftPaperSurvival plugin;

	public RoleplayTrait() {
		super("fabicraftroleplay");
		this.plugin = JavaPlugin.getPlugin(FabiCraftPaperSurvival.class);
	}

	@EventHandler
	public void click(NPCRightClickEvent event) {
		if (event.getNPC() != this.getNPC()) {
			return;
		}

		new RoleplayDialog(this.plugin, event.getClicker()).show();
	}
}
