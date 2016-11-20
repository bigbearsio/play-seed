organization := "io.bigbears"
name := "play-seed"
version := "1.0.0"

sources in(Compile, doc) := Seq.empty
publishArtifact in(Compile, packageDoc) := false

scalaVersion := "2.11.8"
scalacOptions += "-feature"
scalacOptions += "-deprecation"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(
  jdbc
    exclude("com.h2database", "h2")
    exclude("com.jolbox", "bonecp")
  , cache, ws
  , "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.4"
  , "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
  , "de.svenkubiak" % "jBCrypt" % "0.4.1"
  , "net.codingwell" %% "scala-guice" % "4.1.0"
)

parallelExecution := true
parallelExecution in Test := true
libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

excludeDependencies ++= Seq(
  SbtExclusionRule("com.typesafe.play", "play-java")
)

mappings in Universal := {
  val origMappings = (mappings in Universal).value
  origMappings.filterNot { case (_, file) => file.endsWith("application.conf") || file.endsWith("logback.xml") }
}

import com.typesafe.sbt.packager.universal.ZipHelper
packageBin in Universal := {
  val originalFileName = (packageBin in Universal).value
  val (base, ext) = originalFileName.baseAndExt
  val newFileName = file(originalFileName.getParent) / (base + "_dist." + ext)
  val extractedFiles = IO.unzip(originalFileName, file(originalFileName.getParent))
  val mappings: Set[(File, String)] = extractedFiles.map(f => (f, f.getAbsolutePath.substring(originalFileName.getParent.size + base.size + 2)))
  val binFiles = mappings.filter { case (file, path) => path.startsWith("bin/") }
  for (f <- binFiles) f._1.setExecutable(true)
  ZipHelper.zip(mappings, newFileName)
  IO.move(newFileName, originalFileName)
  IO.delete(file(originalFileName.getParent + "/" + originalFileName.base))
  originalFileName
}
