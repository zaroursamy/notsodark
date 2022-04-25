// See https://pbassiner.github.io/blog/defining_multi-project_builds_with_sbt.html for multi sbt project

ThisBuild / version := "1.0.0"

ThisBuild / scalaVersion := "2.13.8"

val KafkaVersion = "3.1.0"
val AkkaVersion = "2.6.19"
val AkkaHttpVersion = "10.2.9"
val ScalaTestVersion = "3.2.11"
val TypesafeVersion = "1.4.2"
val LogbackVersion = "1.2.10"
val ScalaLoggingVersion = "3.9.4"
val ScalazVersion = "7.3.6"
val KafkaAvroSerVersion = "5.3.0"
val Avro4sVersion = "4.0.12"
val Parquet4sVersion = "2.4.1"
val HadoopVersion = "3.3.2"
val SparkVersion = "3.2.1"
val JacksonVersion = "2.12.0"

lazy val commonDependencies = Seq(
  "com.typesafe" % "config" % TypesafeVersion,
  "ch.qos.logback" % "logback-classic" % LogbackVersion,
  "org.scalaz" %% "scalaz-core" % ScalazVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % ScalaLoggingVersion,
  "org.apache.hadoop" % "hadoop-client" % HadoopVersion,
  "org.scalatest" %% "scalatest" % ScalaTestVersion % Test
)

lazy val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-core" % SparkVersion,
  "org.apache.spark" %% "spark-sql" % SparkVersion
)

lazy val jacksonDependencies = Seq(
  "com.fasterxml.jackson.core" % "jackson-core" % JacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-databind" % JacksonVersion,
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.13" % JacksonVersion
)

val kafkaDependencies = Seq(
  "org.apache.kafka" % "kafka-clients" % KafkaVersion,
  "io.confluent" % "kafka-avro-serializer" % KafkaAvroSerVersion,
  "com.sksamuel.avro4s" %% "avro4s-core" % Avro4sVersion
)

val akkaDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion
)

val parquet4sDependencies = Seq(
  "com.github.mjakubowski84" %% "parquet4s-core" % Parquet4sVersion,
  "com.github.mjakubowski84" %% "parquet4s-akka" % Parquet4sVersion)

lazy val settings = Seq(
  scalacOptions ++= Seq(
    "-unchecked",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-encoding",
    "utf8"
  ),
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
    Resolver.sonatypeRepo("public"),
    "Confluent Maven Repo" at "https://packages.confluent.io/maven/",
    Resolver.bintrayRepo("ovotech", "maven"),
    Resolver.sonatypeRepo("releases")
  )
)

lazy val assemblySettings = Seq(
  assembly / assemblyJarName := name.value + ".jar",
  assembly / assemblyMergeStrategy := {
    case PathList("META-INF", xs@_*) => MergeStrategy.discard
    case "reference.conf" => MergeStrategy.concat
    case _ => MergeStrategy.first
  }
)

lazy val global = (project in file("."))
  .settings(settings)
  .aggregate(
    notSoDark,
    darkServer,
    darkStreaming
  )

lazy val notSoDark = (project in file("not-so-dark"))
  .settings(
    settings,
    name := "not-so-dark",
    libraryDependencies ++= commonDependencies
  )

lazy val darkServer = (project in file("dark-server"))
  .dependsOn(notSoDark)
  .settings(
    name := "dark-server",
    settings,
    assemblySettings,
    libraryDependencies ++= akkaDependencies ++ commonDependencies ++ kafkaDependencies
  )
lazy val darkStreaming = (project in file("dark-streaming"))
  .dependsOn(notSoDark, darkServer)
  .settings(
    name := "dark-streaming",
    settings,
    assemblySettings,
    libraryDependencies ++= kafkaDependencies ++ commonDependencies ++ parquet4sDependencies
  )

lazy val darkBatch = (project in file("dark-batch"))
  .dependsOn(notSoDark, darkStreaming)
  .settings(
    name := "dark-batch",
    settings,
    assemblySettings,
    libraryDependencies ++= commonDependencies ++ sparkDependencies,
    dependencyOverrides ++= jacksonDependencies
  )


