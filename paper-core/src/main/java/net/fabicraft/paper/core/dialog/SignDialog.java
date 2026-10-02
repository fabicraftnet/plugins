package net.fabicraft.paper.core.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.fabicraft.common.locale.BrandColor;
import net.fabicraft.paper.core.FabiCraftPaperCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public final class SignDialog extends DialogFactory {
	private final Sign sign;
	private final MiniMessage miniMessage;

	public SignDialog(FabiCraftPaperCore plugin, Player player, Sign sign) {
		super(player);
		this.miniMessage = plugin.miniMessage();
		this.sign = sign;
	}

	@Override
	public void show() {
		SignSide side = this.sign.getTargetSide(super.player);
		Dialog dialog = Dialog.create(builder -> builder.empty()
				.base(DialogBase.builder(render(Component.translatable("fabicraft.paper.core.dialog.sign.title")))
						.body(List.of(
								DialogBody.plainMessage(render(Component.translatable()
												.key("fabicraft.paper.core.dialog.sign.body")
												.arguments(Component.text()
														.content("MiniMessage")
														.color(BrandColor.ACCENT.textColor)
														.clickEvent(ClickEvent.openUrl("https://docs.papermc.io/adventure/minimessage/format/"))
												).build()
										)
								)
						))
						.inputs(List.of(
										DialogInput
												.text("0", Component.empty())
												.initial(this.miniMessage.serialize(side.line(0)))
												.labelVisible(false)
												.build(),
										DialogInput
												.text("1", Component.empty())
												.initial(this.miniMessage.serialize(side.line(1)))
												.labelVisible(false)
												.build(),
										DialogInput
												.text("2", Component.empty())
												.initial(this.miniMessage.serialize(side.line(2)))
												.labelVisible(false)
												.build(),
										DialogInput
												.text("3", Component.empty())
												.initial(this.miniMessage.serialize(side.line(3)))
												.labelVisible(false)
												.build()
								)
						).build())
				.type(DialogType.notice(ActionButton.create(
						render(Component.translatable("fabicraft.paper.core.dialog.sign.save")),
						null,
						100,
						DialogAction.customClick(
								(view, _) -> save(view, sign, side),
								ClickCallback.Options.builder()
										.uses(1)
										.lifetime(ClickCallback.DEFAULT_LIFETIME)
										.build()
						)
				)))
		);
		super.player.showDialog(dialog);
	}

	private void save(DialogResponseView view, Sign sign, SignSide side) {
		for (int i = 0; i < 4; i++) {
			side.line(i, this.miniMessage.deserialize(Objects.requireNonNullElse(view.getText(String.valueOf(i)), "")));
		}
		sign.update();
	}
}
