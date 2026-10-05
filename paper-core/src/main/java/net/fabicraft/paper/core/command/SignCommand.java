package net.fabicraft.paper.core.command;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.core.FabiCraftPaperCore;
import net.fabicraft.paper.core.command.parser.DyeColorParser;
import net.fabicraft.paper.core.dialog.SignDialog;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.DyeColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.parser.standard.BooleanParser;


public final class SignCommand extends PaperCommand<FabiCraftPaperCore> {
	private static final String PERMISSION = "fabicraft.paper.core.command.sign";
	private static final String PERMISSION_GLOWING = PERMISSION + ".glowing";
	private static final String PERMISSION_COLOR = PERMISSION + ".color";

	private static final Component COMPONENT_ERROR = Components.translatable(
			"fabicraft.paper.core.command.sign.error",
			MessageType.ERROR
	);
	private static final Component COMPONENT_GLOWING_TRUE = Components.translatable(
			"fabicraft.paper.core.command.sign.glowing.true",
			MessageType.SUCCESS
	);
	private static final Component COMPONENT_GLOWING_FALSE = Components.translatable(
			"fabicraft.paper.core.command.sign.glowing.false",
			MessageType.SUCCESS
	);
	private static final TranslatableComponent COMPONENT_COLOR = Components.translatable(
			"fabicraft.paper.core.command.sign.color",
			MessageType.SUCCESS
	);

	public SignCommand(FabiCraftPaperCore plugin) {
		super(plugin);
	}

	@Override
	public void register(PaperCommandManager<Source> manager) {
		var builder = manager.commandBuilder("sign").senderType(PlayerSource.class).permission(PERMISSION);

		manager.command(builder.handler(this::executeShowDialog));
		manager.command(builder
				.literal("glowing")
				.permission(PERMISSION_GLOWING)
				.optional("glowing", BooleanParser.booleanParser())
				.handler(this::executeGlowing)
		);

		manager.command(builder.literal("color")
				.permission(PERMISSION_COLOR)
				.required("color", DyeColorParser.dyeColorParser())
				.handler(this::executeColor)
		);
	}

	private void executeShowDialog(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		Sign sign = targetedSign(player);
		if (sign == null) {
			player.sendMessage(COMPONENT_ERROR);
			return;
		}
		new SignDialog(super.plugin, player, sign).show();
	}

	private void executeGlowing(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		Sign sign = targetedSign(player);
		if (sign == null) {
			player.sendMessage(COMPONENT_ERROR);
			return;
		}

		SignSide side = sign.getTargetSide(player);
		boolean glowing = context.getOrDefault("glowing", !side.isGlowingText());
		side.setGlowingText(glowing);
		sign.update();
		player.sendMessage(glowing ? COMPONENT_GLOWING_TRUE : COMPONENT_GLOWING_FALSE);
	}

	private void executeColor(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		Sign sign = targetedSign(player);
		if (sign == null) {
			player.sendMessage(COMPONENT_ERROR);
			return;
		}
		DyeColor color = context.get("color");
		sign.getTargetSide(player).setColor(color);
		player.sendMessage(COMPONENT_COLOR.arguments(Component.text(color.toString(), TextColor.color(color.getColor().asRGB()))));

		sign.update();
	}

	private Sign targetedSign(Player player) {
		Block targetBlock = player.getTargetBlockExact(10, FluidCollisionMode.NEVER);
		if (targetBlock == null) {
			return null;
		}
		return targetBlock.getState() instanceof Sign sign ? sign : null;
	}
}
