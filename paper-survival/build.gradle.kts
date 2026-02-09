import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml

plugins {
	id("fabicraft.paper-conventions")
}

description = "Survival plugin"
version = "1"

dependencies {
	implementation(project(":paper-common"))
	compileOnly(libs.plugin.carbon)
}

paperPluginYaml {
	main = "net.fabicraft.paper.survival.FabiCraftPaperSurvival"
	name = prefixedPluginName
	author = "FabianAdrian"
	apiVersion = "1.21.11"
	dependencies {
		server {
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
		}
	}
}