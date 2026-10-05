import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml

plugins {
	id("fabicraft.paper-conventions")
}

version = "1"
description = "Main paper plugin"

dependencies {
	api(project(":common"))
	api(libs.cloud.paper)
	compileOnly(libs.plugin.huskhomes)
	compileOnlyApi(libs.platform.paper)
	compileOnlyApi(libs.plugin.miniplaceholders)
}

paperPluginYaml {
	main = "net.fabicraft.paper.core.FabiCraftPaperCore"
	name = prefixedPluginName
	author = "FabianAdrian"
	apiVersion = "1.21.11"
	dependencies {
		server {
			register("MiniPlaceholders") {
				load = PaperPluginYaml.Load.BEFORE
				required = true
			}
			register("LuckPerms") {
				load = PaperPluginYaml.Load.BEFORE
				required = true
			}
			register("HuskHomes") {
				load = PaperPluginYaml.Load.BEFORE
				required = false
			}
		}
	}
	permissions {
		register("fabicraft.paper.core.command.bonk")
		register("fabicraft.paper.core.command.bonk.broadcast")
		register("fabicraft.paper.core.command.bonk.broadcast.sender")
		register("fabicraft.paper.core.command.builder.nightvision")
		register("fabicraft.paper.core.command.crafter")
		register("fabicraft.paper.core.command.fabicraft.reload")
		register("fabicraft.paper.core.command.sign")
		register("fabicraft.paper.core.command.sign.glowing")
		register("fabicraft.paper.core.command.sign.color")

		register("fabicraft.paper.core.join.bypass")
	}
}