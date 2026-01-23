import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml

plugins {
	id("fabicraft.paper-conventions")
}

description = "Survival plugin"
version = "1"

dependencies {
	compileOnly(project(":paper"))
}

paperPluginYaml {
	main = "net.fabicraft.survival.FabiCraftSurvival"
	name = prefixedPluginName
	author = "FabianAdrian"
	apiVersion = "1.21.11"
	dependencies {
		server {
			register("FabiCraft-Paper") {
				required = true
				load = PaperPluginYaml.Load.BEFORE
			}
			register("LuckPerms") {
				required = true
				load = PaperPluginYaml.Load.BEFORE
			}
		}
	}
}