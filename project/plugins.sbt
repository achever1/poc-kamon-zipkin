addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.17")

resolvers += Resolver.bintrayRepo("kamon-io", "sbt-plugins")
addSbtPlugin("io.kamon" % "sbt-aspectj-runner" % "1.1.0")
addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent" % "0.1.4")