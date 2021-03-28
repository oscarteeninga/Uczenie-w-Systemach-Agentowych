name := "Uczenie-w-Systemach-Agentowych"

version := "0.1"

scalaVersion := "2.11.11"

val AkkaVersion = "2.4.20"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.thoughtworks.deeplearning" %% "plugins-builtins" % "latest.release"

libraryDependencies += "org.nd4j" % "nd4j-native" % "1.0.0-beta7" % Test
libraryDependencies += "org.nd4j" % "nd4j-native-platform" % "1.0.0-beta7"
libraryDependencies += "org.nd4j" % "nd4j-tensorflow" % "1.0.0-beta7"
libraryDependencies += "org.deeplearning4j" % "deeplearning4j-core" % "1.0.0-beta7"
libraryDependencies += "org.deeplearning4j" % "deeplearning4j-nlp" % "1.0.0-beta7"
libraryDependencies += "org.bytedeco" % "tensorflow-platform" % "1.15.5-1.5.5"

fork := true

