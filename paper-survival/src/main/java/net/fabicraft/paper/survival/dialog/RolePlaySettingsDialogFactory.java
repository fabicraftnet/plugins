package net.fabicraft.paper.survival.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.fabicraft.paper.common.luckperms.PaperLuckPermsManager;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.command.commands.RolePlayCommand;
import net.fabicraft.paper.survival.player.PlayerData;
import net.fabicraft.paper.survival.player.PlayerDataManager;
import net.fabicraft.paper.survival.player.PlayerHeightController;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public final class RolePlaySettingsDialogFactory {
	private final PaperLuckPermsManager luckPermsManager;
	private final PlayerDataManager playerDataManager;
	private final PlayerHeightController playerHeightController = new PlayerHeightController();

	public RolePlaySettingsDialogFactory(FabiCraftPaperSurvival plugin) {
		this.luckPermsManager = plugin.luckPermsManager();
		this.playerDataManager = plugin.playerDataManager();
	}

	public Dialog dialog(Player player) {
		PlayerData data = this.playerDataManager.data(player);
		if (data == null) {
			return PlayerDataNotLoadedDialog.dialog();
		}

		return Dialog.create(builder -> builder.empty()
				.base(DialogBase.builder(Component.text("Roolipeliasetukset"))
						.inputs(List.of(
										DialogInput.bool("enabled", Component.text("Roolipeliominaisuudet"))
												.initial(this.luckPermsManager.hasGroup(player, "roleplay"))
												.onFalse("Poissa käytöstä")
												.onTrue("Käytössä")
												.build(),
										DialogInput.text("name", Component.text("Hahmon nimi"))
												.initial(Objects.requireNonNullElse(data.rolePlayName(), player.getName()))
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
						DialogAction.customClick((view, audience) -> handleSave(view, player, data), ClickCallback.Options.builder()
								.uses(1)
								.lifetime(ClickCallback.DEFAULT_LIFETIME)
								.build())
				)))
		);
	}

	private void handleSave(DialogResponseView view, Player player, PlayerData data) {
		Boolean enabled = Objects.requireNonNull(view.getBoolean("enabled"), "enabled must not be null");
		int height = Objects.requireNonNull(view.getFloat("height"), "height must not be null").intValue();
		String name = Objects.requireNonNull(view.getText("name"), "name must not be null");

		data.rolePlayHeight(height);
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
