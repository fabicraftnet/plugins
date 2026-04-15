package net.fabicraft.paper.survival.npc;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.dialog.RolePlaySettingsDialogFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

@TraitName("fabicraftroleplay")
public final class RoleplayTrait extends Trait {
	private final RolePlaySettingsDialogFactory dialogFactory;

	public RoleplayTrait() {
		super("fabicraftroleplay");
		FabiCraftPaperSurvival plugin = JavaPlugin.getPlugin(FabiCraftPaperSurvival.class);
		this.dialogFactory = new RolePlaySettingsDialogFactory(plugin);
	}

	@EventHandler
	public void click(NPCRightClickEvent event) {
		if (event.getNPC() != this.getNPC()) {
			return;
		}

		Player player = event.getClicker();
		player.showDialog(this.dialogFactory.dialog(player));
	}
}
