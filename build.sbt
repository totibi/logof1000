lazy val server = (project in file("server")).settings(commonSettings).settings(
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http" % "10.0.10",
    "com.vmunier" %% "scalajs-scripts" % "1.1.0"
  ),
  managedClasspath in Runtime += (packageBin in Assets).value,
).enablePlugins(SbtWeb, JavaAppPackaging).
  dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(commonSettings).settings(
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.3"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSWeb).
  dependsOn(sharedJs)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).settings(commonSettings)
lazy val sharedJs = shared.js
lazy val sharedJvm = shared.jvm

lazy val commonSettings = Seq(
	scalaVersion := "2.12.2",
	organization := "com.memories",
	libraryDependencies ++= Seq(
		"com.lihaoyi" %%% "scalatags" % "0.6.2",
		"com.lihaoyi" %%% "upickle" % "0.5.1",
		"com.lihaoyi" %%% "autowire" % "0.2.6"
	)
)

// loads the server project at sbt startup
onLoad in Global := (onLoad in Global).value andThen {s: State => "project server" :: s}
