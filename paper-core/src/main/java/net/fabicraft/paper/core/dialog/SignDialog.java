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
import org.bukkit.NamespacedKey;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public final class SignDialog extends DialogFactory {
	private static final int SIGN_LINE_COUNT = 4;
	private final Sign sign;
	private final MiniMessage miniMessage;
	private final NamespacedKey frontKey;
	private final NamespacedKey backKey;

	public SignDialog(FabiCraftPaperCore plugin, Player player, Sign sign) {
		super(player);
		this.miniMessage = plugin.miniMessage();
		this.sign = sign;
		this.frontKey = new NamespacedKey(plugin, "sign_lines_front");
		this.backKey = new NamespacedKey(plugin, "sign_lines_back");
	}

	@Override
	public void show() {
		SignSide side = this.sign.getTargetSide(super.player);
		List<String> raw = sign.getPersistentDataContainer().get(
				keyFor(sign, side),
				PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING)
		);

		if (raw == null) {
			List<String> fallback = new ArrayList<>(SIGN_LINE_COUNT);
			for (int i = 0; i < SIGN_LINE_COUNT; i++) {
				fallback.add(super.plainTextComponentSerializer.serialize(side.line(i)));
			}
			raw = fallback;
		}

		List<DialogInput> inputs = new ArrayList<>(SIGN_LINE_COUNT);
		for (int i = 0; i < SIGN_LINE_COUNT; i++) {
			inputs.add(DialogInput
					.text(String.valueOf(i), Component.empty())
					.initial(raw.get(i).substring(0, Math.min(99, raw.get(i).length())))
					.maxLength(99)
					.labelVisible(false)
					.build());
		}

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
						.inputs(inputs).build())
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
		List<String> raw = new ArrayList<>(SIGN_LINE_COUNT);
		for (int i = 0; i < SIGN_LINE_COUNT; i++) {
			String text = Objects.requireNonNullElse(view.getText(String.valueOf(i)), "");
			raw.add(text);
			side.line(i, this.miniMessage.deserialize(text));
		}
		sign.getPersistentDataContainer().set(
				keyFor(sign, side),
				PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING),
				raw
		);
		sign.update();
	}

	private NamespacedKey keyFor(Sign sign, SignSide side) {
		return sign.getSide(Side.FRONT).equals(side) ? frontKey : backKey;
	}
}
