version := "0.1"
scalaVersion := Versions.scala

ThisBuild / scapegoatVersion := Versions.scapegoat
scapegoatReports := Seq("xml")
Scapegoat / scalacOptions += "-P:scapegoat:overrideLevels:all=Warning"

lazy val `pandora` = project
  .in(file("."))
  .settings(
    publishArtifact := true,
    libraryDependencies ++= Dependencies.json4s,
    libraryDependencies += Dependencies.scalaXml,
    libraryDependencies ++= Dependencies.specs2
  )
  .settings(Common.commonSettings: _*)
