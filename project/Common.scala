import sbt.Keys._
import sbt._

object Common {

  lazy val commonSettings = Seq(
    organization := "com.credimi",
    scalacOptions ++= Seq(
      "-Xfatal-warnings",
      "-Ywarn-unused",
      "-feature",
      "-deprecation"
    )
  )
}
