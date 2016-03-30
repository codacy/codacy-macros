import sbt._

object Dependencies{

  val dropWizardVersion = "3.1.2"
  val playVersion = "2.4.3"

  val metricsCore     = "io.dropwizard.metrics" %  "metrics-core"     % dropWizardVersion
  val metricsJson     = "io.dropwizard.metrics" %  "metrics-json"     % dropWizardVersion
  val metricsGraphite = "io.dropwizard.metrics" %  "metrics-graphite" % dropWizardVersion
  val metricsServlets = "io.dropwizard.metrics" %  "metrics-servlets" % dropWizardVersion
  val play            = "com.typesafe.play"     %% "play"             % playVersion
  val playJdbc        = "com.typesafe.play"     %% "play-jdbc"        % playVersion
  val playWs          = "com.typesafe.play"     %% "play-ws"          % playVersion
  val playJson        = "com.typesafe.play"     %% "play-json"        % playVersion
  val macroParadise   = "org.scalamacros"       %% "paradise"         % "2.1.0"

  //Test
  val specs2 = "org.specs2" %% "specs2-core" % "3.7" % "test"

}

