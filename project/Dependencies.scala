import sbt._

object Dependencies{

  def playJson(playVersion: String) = "com.typesafe.play" %% "play-json" % playVersion

  val macroParadise = "org.scalamacros" %% "paradise" % "2.1.1"

  //Test
  val specs2 = "org.specs2" %% "specs2-core" % "3.8.9" % Test

}
