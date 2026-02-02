package net.fabicraft.paper.core.command;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.core.FabiCraftPaperCore;
import net.fabicraft.paper.common.command.PaperCommand;
import net.fabicraft.paper.common.command.parser.DyeColorParser;
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
import org.incendo.cloud.minecraft.extras.parser.ComponentParser;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.parser.standard.BooleanParser;
import org.incendo.cloud.parser.standard.IntegerParser;
import org.incendo.cloud.parser.standard.StringParser;


public final class SignCommand extends PaperCommand<FabiCraftPaperCore> {
	private static final String PERMISSION = "fabicraft.paper.core.command.sign";
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
	private static final TranslatableComponent COMPONENT_COLOR_SET = Components.translatable(
			"fabicraft.paper.core.command.sign.color.set",
			MessageType.SUCCESS
	);

	private static final Component COMPONENT_COLOR_CLEAR = Components.translatable(
			"fabicraft.paper.core.command.sign.color.clear",
			MessageType.SUCCESS
	);
	private static final TranslatableComponent COMPONENT_LINE = Components.translatable(
			"fabicraft.paper.core.command.sign.line",
			MessageType.SUCCESS
	);

	public SignCommand(FabiCraftPaperCore plugin) {
		super(plugin, plugin.commandManager());
	}

	@Override
	public void register() {
		var builder = super.manager.commandBuilder("sign").senderType(PlayerSource.class).permission(PERMISSION);

		super.manager.command(builder
				.literal("glowing")
				.optional("glowing", BooleanParser.booleanParser())
				.handler(this::executeGlowing)
		);

		var colorBuilder = builder.literal("color");
		super.manager.command(colorBuilder
				.literal("set")
				.required("color", DyeColorParser.dyeColorParser())
				.handler(this::executeColorSet)
		);
		super.manager.command(colorBuilder
				.literal("clear")
				.handler(this::executeColorClear)
		);

		super.manager.command(builder
				.literal("line")
				.required("line", IntegerParser.integerParser(1, 4))
				.required("text", ComponentParser.miniMessageParser(StringParser.StringMode.GREEDY))
				.handler(this::executeLine)
		);
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

	private void executeColorSet(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		Sign sign = targetedSign(player);
		if (sign == null) {
			player.sendMessage(COMPONENT_ERROR);
			return;
		}
		DyeColor color = context.getOrDefault("color", null);
		sign.getTargetSide(player).setColor(color);
		sign.update();
		player.sendMessage(COMPONENT_COLOR_SET.arguments(Component.text(color.toString(), TextColor.color(color.getColor().asRGB()))));
	}

	private void executeColorClear(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		Sign sign = targetedSign(player);
		if (sign == null) {
			player.sendMessage(COMPONENT_ERROR);
			return;
		}
		sign.getTargetSide(player).setColor(null);
		sign.update();
		player.sendMessage(COMPONENT_COLOR_CLEAR);
	}

	private void executeLine(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();

		Sign sign = targetedSign(player);
		if (sign == null) {
			player.sendMessage(COMPONENT_ERROR);
			return;
		}

		int lineNumber = context.get("line");
		Component text = context.get("text");

		sign.getTargetSide(player).line(--lineNumber, text);
		sign.update();

		player.sendMessage(COMPONENT_LINE.arguments(text));
	}

	private Sign targetedSign(Player player) {
		Block targetBlock = player.getTargetBlockExact(10, FluidCollisionMode.NEVER);
		if (targetBlock == null) {
			return null;
		}
		return targetBlock.getState() instanceof Sign sign ? sign : null;

	}
}
