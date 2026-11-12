import org.gradle.kotlin.dsl.support.uppercaseFirstChar

plugins {
	id("fabicraft.typewriter-conventions")
}

version = "1"
description = "FabiCraft survival typewriter extension"

dependencies {
	compileOnly(project(":paper-survival"))
}

typewriter {
	namespace = "fabicraft"
	extension {
		engineVersion = "0.9.0"
		channel = com.typewritermc.moduleplugin.ReleaseChannel.BETA
		name = "FabiCraft${project.name.uppercaseFirstChar()}"
		shortDescription = "FabiCraft survival extension"
		description = """
			|FabiCraft survival typewriter extension that integrates our custom stuff into typewriter
			|This description must be over 100 characters or typewriter will reject our extension
		""".trimIndent()
		paper {
			dependency("FabiCraft-Paper-Survival")
		}
	}
}