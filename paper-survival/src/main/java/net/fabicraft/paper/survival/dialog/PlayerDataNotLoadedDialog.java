package net.fabicraft.paper.survival.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.core.dialog.DialogFactory;
import org.bukkit.entity.Player;

import java.util.List;

public final class PlayerDataNotLoadedDialog extends DialogFactory {
	public PlayerDataNotLoadedDialog(Player player) {
		super(player);
	}

	@Override
	public void show() {
		Dialog dialog = Dialog.create(builder -> builder.empty()
				.base(DialogBase.builder(render(Components.translatable("fabicraft.paper.survival.dialog.player-data-not-loaded.title", MessageType.ERROR)))
						.body(List.of(DialogBody.plainMessage(
								render(Components.translatable("fabicraft.paper.survival.dialog.player-data-not-loaded.body", MessageType.INFO, "/ticket")))
						)).build())
				.type(DialogType.notice())
		);
		super.player.showDialog(dialog);
	}
}
