name := "randomDataGenerator"

version := "0.1"

scalaVersion := "2.11.8"

resolvers += Resolver.sonatypeRepo("releases")
libraryDependencies += "com.danielasfregola" %% "random-data-generator" % "2.7"
libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.11.8"
libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.6.7"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0"

mainClass in assembly := some("launch")
assemblyJarName := "randomDataGenerator.jar"
val meta = """META.INF(.)*""".r
assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case n if n.startsWith("reference.conf") => MergeStrategy.concat
  case n if n.endsWith(".conf") => MergeStrategy.concat
  case meta(_) => MergeStrategy.discard
  case x => MergeStrategy.first
}