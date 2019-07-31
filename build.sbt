name := """gender-recognition"""
organization := "springernature"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test,
"org.mongodb.scala" %% "mongo-scala-driver" % "2.4.2",
  "io.netty" % "netty-all" % "4.1.38.Final"
)
