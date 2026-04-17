plugins {
	id("fabicraft.velocity-conventions")
}

version = "1"
description = "School of Gaming network Velocity plugin."

dependencies {
	implementation(project(":common"))

	compileOnly(libs.platform.velocity)

	implementation(libs.cloud.velocity)
	implementation(libs.dazzleconf)
}

velocityPluginJson {
	main = "net.fabicraft.velocity.FabiCraftVelocity"
	id = "fabicraft"
	name = prefixedPluginName
	authors.add("FabianAdrian")
}

tasks {
	shadowJar {
		listOf(
			"com.fasterxml.jackson",
			"io.leangen.geantyref",
			"org.flywaydb",
			"org.incendo.cloud",
			"space.arim.dazzleconf"
		).forEach {
			relocate(it, "net.fabicraft.velocity.dependency.$it")
		}
		mergeServiceFiles()
	}
}