package net.fabicraft.paper.survival.npc;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.fabicraft.paper.common.luckperms.PaperLuckPermsManager;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.command.commands.RolePlayCommand;
import net.fabicraft.paper.survival.player.PlayerData;
import net.fabicraft.paper.survival.player.PlayerDataManager;
import net.fabicraft.paper.survival.player.PlayerHeightController;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

@TraitName("fabicraftroleplay")
public final class RoleplayTrait extends Trait {
	private final PlayerDataManager playerDataManager;
	private final PlayerHeightController playerHeightController = new PlayerHeightController();
	private final PaperLuckPermsManager luckPermsManager;

	public RoleplayTrait() {
		super("fabicraftroleplay");
		FabiCraftPaperSurvival plugin = JavaPlugin.getPlugin(FabiCraftPaperSurvival.class);
		this.playerDataManager = plugin.playerDataManager();
		this.luckPermsManager = plugin.luckPermsManager();
	}

	@EventHandler
	public void click(NPCRightClickEvent event) {
		if (event.getNPC() != this.getNPC()) {
			return;
		}

		PlayerData data = this.playerDataManager.data(event.getClicker().getUniqueId());
		if (data == null) {
			throw new IllegalArgumentException("Player data is null");
		}

		Dialog dialog = Dialog.create(builder -> builder.empty()
				.base(DialogBase.builder(Component.text("Roolipeliasetukset"))
						.inputs(List.of(
										DialogInput.bool("enabled", Component.text("Roolipeliominaisuudet"))
												.initial(this.luckPermsManager.hasGroup(event.getClicker(), "roleplay"))
												.onFalse("Poissa käytöstä")
												.onTrue("Käytössä")
												.build(),
										DialogInput.text("name", Component.text("Hahmon nimi"))
												.initial(Objects.requireNonNullElse(data.rolePlayName(), event.getClicker().getName()))
												.maxLength(20)
												.labelVisible(true)
												.build(),
										DialogInput.numberRange("height", Component.text("Hahmon pituus"), 150, 200)
												.initial((float) Objects.requireNonNullElse(data.rolePlayHeight(), 180))
												.step(1f)
												.labelFormat("%s: %scm")
												.build()
								)
						).build())
				.type(DialogType.notice(ActionButton.create(
						Component.text("Tallenna"),
						Component.text("Tallenna asetukset"),
						100,
						DialogAction.customClick(this::handleSave, ClickCallback.Options.builder()
								.uses(1)
								.lifetime(ClickCallback.DEFAULT_LIFETIME)
								.build())
				)))
		);
		event.getClicker().showDialog(dialog);
	}

	private void handleSave(DialogResponseView view, Audience audience) {
		Boolean enabled = view.getBoolean("enabled");
		if (enabled == null) {
			throw new IllegalArgumentException("enabled is null");
		}
		Float height = view.getFloat("height");
		if (height == null) {
			throw new IllegalArgumentException("height is null");
		}
		String name = view.getText("name");
		if (name == null) {
			throw new IllegalArgumentException("name is null");
		}

		Player player = (Player) audience;
		PlayerData data = this.playerDataManager.data(player.getUniqueId());
		if (data == null) {
			throw new IllegalArgumentException("Player data is null");
		}
		data.rolePlayHeight(height.intValue());
		data.rolePlayName(name);
		this.playerDataManager.save(player.getUniqueId());

		if (enabled) {
			this.playerHeightController.set(player, data.rolePlayHeight());
			if (!this.luckPermsManager.hasGroup(player, "roleplay")) {
				this.luckPermsManager.addGroup(player, "roleplay");
				player.sendMessage(RolePlayCommand.COMPONENT_ADD);
			}
		} else {
			this.playerHeightController.reset(player);
			if (this.luckPermsManager.hasGroup(player, "roleplay")) {
				this.luckPermsManager.removeGroup(player, "roleplay");
				player.sendMessage(RolePlayCommand.COMPONENT_REMOVE);
			}
		}
	}
}
