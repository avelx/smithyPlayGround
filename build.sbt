import smithy4s.codegen.Smithy4sCodegenPlugin

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.16"

lazy val root = (project in file("."))
  .settings(
    name := "smithyPlayGround"
  )

lazy val doobieVersion = "1.0.0-RC8"

val example = project
  .in(file("modules/example"))
  .enablePlugins(Smithy4sCodegenPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "com.disneystreaming.smithy4s" %% "smithy4s-http4s" % smithy4sVersion.value,
      "com.disneystreaming.smithy4s" %% "smithy4s-http4s-swagger" % smithy4sVersion.value,
      "org.http4s" %% "http4s-ember-server" % "0.23.30",

      "org.tpolecat" %% "doobie-core"      % doobieVersion,

      // And add any of these as needed
      "org.tpolecat" %% "doobie-postgres"  % doobieVersion,
    )
  )
  .settings(
    Compile / run / fork := true
  )
