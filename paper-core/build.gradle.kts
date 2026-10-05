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
		register("fabicraft.paper.core.join.bypass")
	}
}