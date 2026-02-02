plugins {
	id("fabicraft.java-conventions")
}

description = "Common code shared between the Paper and Velocity modules"

dependencies {
	compileOnly(libs.adventure)
	compileOnly(libs.adventure.text.minimessage)
	api(libs.cloud.minecraftExtras)
	compileOnly(libs.slf4j)
	compileOnlyApi(libs.plugin.luckperms)
	compileOnlyApi(libs.plugin.miniplaceholders)
}