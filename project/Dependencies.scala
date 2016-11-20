import sbt._

object Dependencies{

  val playVersion = "2.5.9"

  val playJson        = "com.typesafe.play"     %% "play-json"        % playVersion
  val macroParadise   = "org.scalamacros"       %% "paradise"         % "2.1.0"

  //Test
  val specs2 = "org.specs2" %% "specs2-core" % "3.7" % "test"

}

