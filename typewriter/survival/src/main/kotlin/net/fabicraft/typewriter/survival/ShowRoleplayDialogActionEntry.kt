package net.fabicraft.typewriter.survival

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.entry.entries.ActionTrigger
import net.fabicraft.paper.survival.FabiCraftPaperSurvival
import net.fabicraft.paper.survival.dialog.RoleplayDialog
import org.bukkit.Bukkit

@Entry(
	"show_roleplay_dialog",
	"Opens the roleplay dialog for the player.",
	Colors.RED,
	"fa6-solid:message"
)
class ShowRoleplayDialogActionEntry(
	override val id: String = "",
	override val name: String = "",
	override val criteria: List<Criteria> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
) : ActionEntry {
	override fun ActionTrigger.execute() {
		RoleplayDialog(
			Bukkit.getPluginManager().getPlugin("FabiCraftPaperSurvival") as FabiCraftPaperSurvival?,
			player
		).show()
	}
}