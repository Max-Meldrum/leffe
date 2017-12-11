name := "leffe"

version := "0.1"

organization := "se.meldrum.leffe"

scalaVersion := "2.12.4"

libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.0.0"

mainClass in assembly := Some("se.meldrum.leffe.Leffe")
assemblyJarName in assembly := "leffe" + "-" + version.value + ".jar"



    