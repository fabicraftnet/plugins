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
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.player.PlayerData;
import net.fabicraft.paper.survival.player.PlayerDataManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.List;
import java.util.UUID;

@TraitName("fabicraftroleplay")
public final class RoleplayTrait extends Trait {
	private final PlayerDataManager playerDataManager;

	public RoleplayTrait(FabiCraftPaperSurvival plugin) {
		super("fabicraftroleplay");
		this.playerDataManager = plugin.playerDataManager();
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

		Dialog.create(builder -> builder.empty()
				.base(DialogBase.builder(Component.translatable("fabicraftpapersurvival.dialog.roleplay.title"))
						.inputs(List.of(
										DialogInput.text("name", Component.translatable("fabicraftpapersurvival.dialog.roleplay.label.name"))
												.initial(data.rolePlayName())
												.maxLength(20)
												.labelVisible(true)
												.build(),
										DialogInput.numberRange("height", Component.translatable("fabicraftpapersurvival.dialog.roleplay.label.height"), 150, 200)
												.initial(180f)
												.step(1f)
												.labelFormat("%s: %scm")
												.build()
								)
						).build())
				.type(DialogType.notice(ActionButton.create(
						Component.translatable("fabicraftpapersurvival.dialog.general.button.save"),
						Component.translatable("fabicraftpapersurvival.dialog.general.button.save.description"),
						100,
						DialogAction.customClick(this::handleSave, ClickCallback.Options.builder()
								.uses(1)
								.lifetime(ClickCallback.DEFAULT_LIFETIME)
								.build())
				)))
		);
	}

	private void handleSave(DialogResponseView view, Audience audience) {
		Float height = view.getFloat("height");
		if (height == null) {
			throw new IllegalArgumentException("height is null");
		}
		String name = view.getText("name");
		if (name == null) {
			throw new IllegalArgumentException("name is null");
		}

		UUID uuid = ((Player) audience).getUniqueId();
		PlayerData data = this.playerDataManager.data(uuid);
		if (data == null) {
			throw new IllegalArgumentException("Player data is null");
		}
		data.rolePlayHeight(height.intValue());
		data.rolePlayName(name);
		this.playerDataManager.save(uuid);
	}
}
