plugins {
	id("fabicraft.paper-conventions")
}

dependencies {
	implementation(project(":common"))
	compileOnly(libs.platform.paper)

	compileOnly(libs.plugin.miniplaceholders)
	implementation(libs.cloud.paper)
	implementation(libs.cloud.minecraftExtras)
}

paperPluginYaml {
	main = "net.fabicraft.paper.FabiCraftPaper"
	name = prefixedPluginName
	author = "FabianAdrian"
	apiVersion = "1.21.11"
}