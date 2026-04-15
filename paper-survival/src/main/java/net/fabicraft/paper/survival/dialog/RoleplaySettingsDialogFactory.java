package net.fabicraft.paper.survival.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.common.luckperms.PaperLuckPermsManager;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.command.commands.RolePlayCommand;
import net.fabicraft.paper.survival.config.section.RoleplaySection;
import net.fabicraft.paper.survival.player.PlayerData;
import net.fabicraft.paper.survival.player.PlayerDataManager;
import net.fabicraft.paper.survival.player.PlayerHeightController;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public final class RoleplaySettingsDialogFactory {
	private final PaperLuckPermsManager luckPermsManager;
	private final PlayerDataManager playerDataManager;
	private final PlayerHeightController playerHeightController = new PlayerHeightController();
	private final FabiCraftPaperSurvival plugin;

	public RoleplaySettingsDialogFactory(FabiCraftPaperSurvival plugin) {
		this.plugin = plugin;
		this.luckPermsManager = plugin.luckPermsManager();
		this.playerDataManager = plugin.playerDataManager();
	}

	public Dialog dialog(Player player) {
		PlayerData data = this.playerDataManager.data(player);
		if (data == null) {
			return PlayerDataNotLoadedDialog.dialog();
		}

		RoleplaySection config = this.plugin.config().roleplay();

		return Dialog.create(builder -> builder.empty()
				.base(DialogBase.builder(Component.text("Roolipeliasetukset"))
						.inputs(List.of(
										DialogInput.bool("enabled", Component.text("Roolipeliominaisuudet"))
												.initial(this.luckPermsManager.hasGroup(player, "roleplay"))
												.onFalse("Poissa käytöstä")
												.onTrue("Käytössä")
												.build(),
										DialogInput.text("name", Component.text("Hahmon nimi"))
												.initial(Objects.requireNonNullElse(data.characterName(), player.getName()))
												.maxLength(config.maxNameLength())
												.labelVisible(true)
												.build(),
										DialogInput.numberRange("height", Component.text("Hahmon pituus"), config.minHeight(), config.maxHeight())
												.initial((float) Objects.requireNonNullElse(data.characterHeight(), PlayerHeightController.DEFAULT_HEIGHT))
												.step(1f)
												.labelFormat("%s: %scm")
												.build()
								)
						).build())
				.type(DialogType.notice(ActionButton.create(
						Component.text("Tallenna"),
						Component.text("Tallenna asetukset"),
						100,
						DialogAction.customClick(
								(view, audience) -> handleSave(view, player, data),
								ClickCallback.Options.builder()
										.uses(1)
										.lifetime(ClickCallback.DEFAULT_LIFETIME)
										.build()
						)
				)))
		);
	}

	private void handleSave(DialogResponseView view, Player player, PlayerData data) {
		Boolean enabled = Objects.requireNonNull(view.getBoolean("enabled"), "enabled must not be null");
		int height = Objects.requireNonNull(view.getFloat("height"), "height must not be null").intValue();
		String name = Objects.requireNonNull(view.getText("name"), "name must not be null");

		data.characterHeight(height);
		int minNameLength = this.plugin.config().roleplay().minNameLength();
		if (name.isBlank() || name.length() < minNameLength) {
			player.sendMessage(Components.translatable(
					"fabicraft.paper.survival.dialog.roleplaysettings.name-too-short",
					MessageType.ERROR,
					minNameLength
			));
		} else {
			data.characterName(name);
		}

		this.playerDataManager.save(player.getUniqueId());

		if (enabled) {
			this.playerHeightController.set(player, data.characterHeight());
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
