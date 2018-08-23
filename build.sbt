name := """poc-kamon-zipkin"""
organization := "org.talend"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, JavaAgent)
javaAgents += "org.aspectj" % "aspectjweaver" % "1.8.13"
javaOptions in Universal += "-Dorg.aspectj.tracing.factory=default"

scalaVersion := "2.12.6"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.13"
libraryDependencies += ws
libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.5.14"
libraryDependencies += "com.typesafe.akka" %% "akka-cluster" % "2.5.14"
libraryDependencies += "com.typesafe.akka" %% "akka-cluster-tools" % "2.5.14"
libraryDependencies += "com.typesafe.akka" %% "akka-cluster-metrics" % "2.5.14"
libraryDependencies += "de.heikoseeberger" %% "constructr" % "0.19.0" % Runtime
libraryDependencies += "com.lightbend.constructr" %% "constructr-coordination-zookeeper" % "0.4.1" % Runtime




// Optional Dependencies
libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-core" % "1.1.0",
  "io.kamon" %% "kamon-logback" % "1.0.0",
  "io.kamon" %% "kamon-akka-2.5" % "1.0.1",
  "io.kamon" %% "kamon-akka-remote-2.5" % "1.0.0",
  "io.kamon" %% "kamon-prometheus" % "1.0.0",
  "io.kamon" %% "kamon-zipkin" % "1.0.0",
  "io.kamon" %% "kamon-play-2.6" % "1.1.0",
  "io.kamon" %% "kamon-scala-future" % "1.0.0"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "org.talend.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "org.talend.binders._"
