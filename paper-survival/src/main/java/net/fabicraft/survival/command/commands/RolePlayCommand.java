package net.fabicraft.survival.command.commands;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.luckperms.PaperLuckPermsManager;
import net.fabicraft.survival.FabiCraftSurvival;
import net.fabicraft.survival.command.PaperSurvivalCommand;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.entity.Player;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.util.sender.PlayerSource;

public final class RolePlayCommand extends PaperSurvivalCommand {
	private static final String PERMISSION = "fabicraft.survival.command.roleplay";
	private static final String GROUP_NAME = "roleplay";
	private static final TranslatableComponent COMPONENT_ADD = Components.translatable(
			"fabicraft.survival.command.roleplay.add",
			MessageType.SUCCESS,
			"/rpchat",
			"/nickname"
	);
	private static final TranslatableComponent COMPONENT_REMOVE = Components.translatable(
			"fabicraft.survival.command.roleplay.remove",
			MessageType.SUCCESS
	);
	private final PaperLuckPermsManager luckPermsManager;

	public RolePlayCommand(FabiCraftSurvival plugin) {
		super(plugin);
		this.luckPermsManager = super.plugin.luckPermsManager();
	}

	@Override
	public void register() {
		var builder = super.manager.commandBuilder("roleplay")
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
