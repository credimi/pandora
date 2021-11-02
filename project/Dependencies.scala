import sbt._

object Dependencies {

  val specs2 =
    Seq("specs2-core", "specs2-matcher-extra", "specs2-scalacheck", "specs2-shapeless")
      .map("org.specs2" %% _ % Versions.specs2 % "test")

  val json4s = Seq("json4s-native").map("org.json4s" %% _ % Versions.json4s)

  val scalaXml = "org.scala-lang.modules" %% "scala-xml" % Versions.scalaXml
}
