name := """play-hello-world"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0-RC1" % Test,
  "org.treppo" %% "moco-scala" % "0.5.1" % Test,
  "org.mockito" % "mockito-core" % "2.0.46-beta" % Test
)

javaOptions in Test ++= Seq("-Dconfig.resource=test.conf")

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
