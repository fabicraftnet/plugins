package net.fabicraft.paper.survival.command.commands;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.common.command.PaperCommand;
import net.fabicraft.paper.common.luckperms.PaperLuckPermsManager;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.entity.Player;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.util.sender.PlayerSource;

public final class RolePlayCommand extends PaperCommand<FabiCraftPaperSurvival> {
	private static final String PERMISSION = "fabicraft.paper.survival.command.roleplay";
	private static final String GROUP_NAME = "roleplay";
	private static final TranslatableComponent COMPONENT_ADD = Components.translatable(
			"fabicraft.paper.survival.command.roleplay.add",
			MessageType.SUCCESS,
			"/rpchat",
			"/nickname"
	);
	private static final TranslatableComponent COMPONENT_REMOVE = Components.translatable(
			"fabicraft.paper.survival.command.roleplay.remove",
			MessageType.SUCCESS
	);
	private final PaperLuckPermsManager luckPermsManager;

	public RolePlayCommand(FabiCraftPaperSurvival plugin) {
		super(plugin, plugin.commandManager());
		this.luckPermsManager = plugin.luckPermsManager();
	}

	@Override
	public void register() {
		var builder = super.manager.commandBuilder("roleplay", "rp")
				.permission(PERMISSION)
				.senderType(PlayerSource.class)
				.handler(this::handle);
		super.manager.command(builder);
	}

	public void handle(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		if (this.luckPermsManager.hasGroup(player, GROUP_NAME)) {
			this.luckPermsManager.removeGroup(player, GROUP_NAME);
			player.sendMessage(COMPONENT_REMOVE);
		} else {
			this.luckPermsManager.addGroup(player, GROUP_NAME);
			player.sendMessage(COMPONENT_ADD);
		}
	}
}
