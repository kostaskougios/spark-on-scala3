/** This build has different sections for each integration. I.e. an http4s section and a kafka section. These sections are not related to each other, please
  * examine the section you're interested in.
  */
val scala3Version = "3.3.1"

ThisBuild / version      := "0.1"
ThisBuild / organization := "io.github.kostaskougios"
name                     := "spark-on-scala3"
ThisBuild / scalaVersion := scala3Version
ThisBuild / scalacOptions ++= Seq("-unchecked", "-feature", "-deprecation")

ThisBuild / resolvers += "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"

// -----------------------------------------------------------------------------------------------
// Dependencies
// -----------------------------------------------------------------------------------------------

val ScalaTest = "org.scalatest" %% "scalatest"   % "3.2.15"     % Test
val Mockito   = "org.mockito"    % "mockito-all" % "2.0.2-beta" % Test

val LogBack  = "ch.qos.logback" % "logback-classic" % "1.4.14"
val Slf4jApi = "org.slf4j"      % "slf4j-api"       % "2.0.9"

val SparkSql = ("org.apache.spark" %% "spark-sql" % "3.5.0" % "provided").cross(CrossVersion.for3Use2_13).exclude("org.scala-lang.modules", "scala-xml_2.13")
//val SparkSql       = "org.apache.spark" % "spark-sql_2.13" % "3.5.0" % "provided"
val SparkScala3Fix = Seq(
  "io.github.vincenzobaz" %% "spark-scala3-encoders" % "0.2.5",
  "io.github.vincenzobaz" %% "spark-scala3-udf"      % "0.2.5"
).map(_.exclude("org.scala-lang.modules", "scala-xml_2.13"))

// -----------------------------------------------------------------------------------------------
// Modules
// -----------------------------------------------------------------------------------------------
val commonSettings = Seq(
  Test / fork := true,
  Test / javaOptions ++= Seq("--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED")
)

lazy val `spark-on-scala3` = project
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      ScalaTest,
      SparkSql,
      LogBack % Test
    ) ++ SparkScala3Fix
  )
