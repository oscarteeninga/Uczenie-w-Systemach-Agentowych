name := "Uczenie-w-Systemach-Agentowych"

version := "0.1"

scalaVersion := "2.11.11"

val AkkaVersion = "2.4.20"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.thoughtworks.deeplearning" %% "plugins-builtins" % "latest.release"
libraryDependencies += "org.nd4j" % "nd4j-native-platform" % "0.8.0"
addCompilerPlugin("com.thoughtworks.import" %% "import" % "latest.release")

libraryDependencies += "com.thoughtworks.each" %% "each" % "latest.release"
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

fork := true

