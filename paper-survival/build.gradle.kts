import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml

plugins {
	id("fabicraft.paper-conventions")
}

description = "Survival plugin"
version = "1"

dependencies {
	compileOnly(project(":paper-core"))
	compileOnly(libs.plugin.carbon)
	compileOnly(libs.plugin.citizens) {
		exclude(group = "*", module = "*")
	}
	implementation(libs.flyway.core)
	implementation(libs.storage.sqlite)
	implementation(libs.inventoryframework)
}

paperPluginYaml {
	main = "net.fabicraft.paper.survival.FabiCraftPaperSurvival"
	name = prefixedPluginName
	author = "FabianAdrian"
	apiVersion = "1.21.11"
	dependencies {
		server {
			register("FabiCraft-Paper-Core") {
				required = true
				load = PaperPluginYaml.Load.BEFORE
			}
			register("LuckPerms") {
				required = true
				load = PaperPluginYaml.Load.BEFORE
			}
			register("MiniPlaceholders") {
				required = true
				load = PaperPluginYaml.Load.BEFORE
			}
			register("CarbonChat") {
				required = true
				load = PaperPluginYaml.Load.BEFORE
			}
			register("Citizens") {
				required = false
				load = PaperPluginYaml.Load.BEFORE
			}
		}
	}
	permissions {
		register("fabicraft.paper.survival.command.fabicraft.item")
		register("fabicraft.paper.survival.command.gathering.add")
		register("fabicraft.paper.survival.command.gathering.remove")
		register("fabicraft.paper.survival.command.gathering.list")
		register("fabicraft.paper.survival.command.gathering.edit")
		register("fabicraft.paper.survival.command.roleplay")
		register("fabicraft.paper.survival.command.roleplay.name")
		register("fabicraft.paper.survival.command.roleplay.height")
	}
}