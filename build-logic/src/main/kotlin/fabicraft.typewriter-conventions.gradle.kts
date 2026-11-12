import org.gradle.kotlin.dsl.support.uppercaseFirstChar

plugins {
	id("fabicraft.kotlin-conventions")
	id("com.typewritermc.module-plugin")
}

tasks {
	jar {
		destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs/typewriter"))
		archiveFileName.set("FabiCraft${project.name.uppercaseFirstChar()}Extension.jar")
	}
}