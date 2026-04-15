package net.fabicraft.paper.survival.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.fabicraft.common.locale.BrandColor;
import net.kyori.adventure.text.Component;

import java.util.List;

public final class PlayerDataNotLoadedDialog {
	private static final Dialog DIALOG = Dialog.create(builder -> builder.empty()
			.base(DialogBase.builder(Component.text("Pelaajadata ei ole ladattuna", BrandColor.ERROR.textColor))
					.body(List.of(
							DialogBody.plainMessage(Component.text("Voit yrittää palvelimelle liittymistä uudestaan. Mikäli ongelma jatkuu voit avata tiketin Discordissa komennolla ")
									.append(Component.text("/ticket", BrandColor.ACCENT.textColor)))
					)).build())
			.type(DialogType.notice())
	);

	public static Dialog dialog() {
		return DIALOG;
	}
}
