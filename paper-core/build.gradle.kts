import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml

plugins {
	id("fabicraft.paper-conventions")
}

version = "1"
description = "Main paper plugin"

dependencies {
	implementation(project(":paper-common"))
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
		}
	}
	permissions {
		register("fabicraft.paper.core.join.bypass")
	}
}