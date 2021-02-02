val scala211 = "2.11.12"
val scala212 = "2.12.11"

val play24 = "2.4.3"
val play25 = "2.5.19"
val play26 = "2.6.13"
val play27 = "2.7.3"

def playMajorMinor(playVersion: String) = playVersion.split('.').take(2).mkString(".")
lazy val playVersion = settingKey[String]("The version of Play used for building.")

ThisBuild / playVersion := play27
ThisBuild / scalaVersion := scala212

/**
  * Given a command it creates an alias to run the command
  * on the entire matrix of play and scala versions.
  * If the command has `:` in it (like test:compile)
  * the alias becomes crossTestCompile instead of crossTest:compile
  * (which is not an allowed sbt alias name)
  */
def addCrossAlias(command: String) = {
  val matrix = Seq(scala211 -> Seq(play24, play25, play26, play27), scala212 -> Seq(play26, play27))

  addCommandAlias(
    s"cross${command.split(':').map(_.capitalize).mkString}",
    matrix
      .flatMap {
        case (scalaV, playVersions) =>
          s"""set ThisBuild / scalaVersion := "$scalaV"""" +: playVersions
            .flatMap(playV => Seq(s"""set ThisBuild / playVersion := "$playV"""", command))
      }
      .mkString(";")
  )
}

// List of crossX aliases.
// Add a command here if you want to call it for
// the entire playVersion / scalaVersion matrix
addCrossAlias("update")
addCrossAlias("compile")
addCrossAlias("test:compile")
addCrossAlias("test")
addCrossAlias("publishLocal")
addCrossAlias("publishSigned")

name := """codacy-macros"""

version := s"${version.value}_play_${playMajorMinor(playVersion.value)}"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

lazy val scalaReflect = Def.setting {
  "org.scala-lang" % "scala-reflect" % scalaVersion.value
}

libraryDependencies ++= Seq(Dependencies.playJson(playVersion.value), Dependencies.specs2)

lazy val commonSettings =
  Seq(target := target.value / playVersion.value, addCompilerPlugin(Dependencies.macroParadise cross CrossVersion.full))

lazy val core = (project in file("."))
  .dependsOn(macroSub % "compile-internal")
  .dependsOn(macroSub % "test")
  .dependsOn(commonSub % "compile-internal")
  .dependsOn(commonSub % "test")
  .settings(commonSettings: _*)
  .settings(
    // include the macro classes and resources in the main jar
    mappings in (Compile, packageBin) ++= (mappings in (macroSub, Compile, packageBin)).value,
    // include the macro sources in the main source jar
    mappings in (Compile, packageSrc) ++= (mappings in (macroSub, Compile, packageSrc)).value,
    // include the macro classes and resources in the main jar
    mappings in (Compile, packageBin) ++= (mappings in (commonSub, Compile, packageBin)).value,
    // include the macro sources in the main source jar
    mappings in (Compile, packageSrc) ++= (mappings in (commonSub, Compile, packageSrc)).value
  )

lazy val macroSub = (project in file("macro"))
  .settings(commonSettings: _*)
  .dependsOn(commonSub)
  .settings(libraryDependencies += scalaReflect.value)
  .settings(publish := {}, publishLocal := {})

lazy val commonSub = (project in file("common"))
  .settings(commonSettings: _*)
  .settings(libraryDependencies += Dependencies.playJson(playVersion.value))
  .settings(publish := {}, publishLocal := {})

organizationName := "Codacy"

organizationHomepage := Some(new URL("https://www.codacy.com"))

publishArtifact in Test := false

startYear := Some(2016)

description := "macros for scala and play-framework"

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("https://github.com/codacy/codacy-macros.git"))

pgpPassphrase := Option(System.getenv("SONATYPE_GPG_PASSPHRASE")).map(_.toCharArray)

scmInfo := Some(
  ScmInfo(url("https://github.com/codacy/codacy-macros"), "scm:git:git@github.com:codacy/codacy-macros.git")
)

publicMvnPublish
