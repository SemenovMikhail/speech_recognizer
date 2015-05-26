name := """play-java"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.4",
  "org.mindrot" % "jbcrypt" % "0.3m",
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)

scalacOptions in Compile in doc += "-diagrams"
