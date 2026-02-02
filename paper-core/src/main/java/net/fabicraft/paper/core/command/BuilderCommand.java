package net.fabicraft.paper.core.command;

import net.fabicraft.paper.core.FabiCraftPaperCore;
import net.fabicraft.paper.common.command.PaperCommand;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.util.sender.PlayerSource;

public final class BuilderCommand extends PaperCommand<FabiCraftPaperCore> {
	private static final String PERMISSION_NIGHTVISION = "fabicraft.paper.core.command.builder.nightvision";

	public BuilderCommand(FabiCraftPaperCore plugin) {
		super(plugin, plugin.commandManager());
	}

	@Override
	public void register() {
		Command.Builder<PlayerSource> builder = this.manager.commandBuilder("builder")
				.senderType(PlayerSource.class);

		this.manager.command(
				builder.literal("nightvision").permission(PERMISSION_NIGHTVISION).handler(this::executeNightVision)
		);
	}

	private void executeNightVision(CommandContext<PlayerSource> ctx) {
		Player player = ctx.sender().source();

		if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
			player.removePotionEffect(PotionEffectType.NIGHT_VISION);
			return;
		}

		PotionEffect nightVision = new PotionEffect(PotionEffectType.NIGHT_VISION, PotionEffect.INFINITE_DURATION, 1, true, false);
		player.addPotionEffect(nightVision);
	}
}
