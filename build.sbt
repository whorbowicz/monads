import sbt.Keys._

lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := Settings.scalaVersion
)

lazy val java = (project in file("monads-java")).
  settings(commonSettings: _*).
  settings(
    name := "monads-java",
    libraryDependencies ++= Dependencies.javaDependencies,
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
  )

lazy val scala = (project in file("monads-scala")).
  settings(commonSettings: _*).
  settings(
    name := "monads-scala",
    libraryDependencies ++= Dependencies.scalaDependencies
  )

lazy val monads = (project in file(".")).
  aggregate(java, scala).
  settings(commonSettings: _*).
  settings(
    name := "monads"
  )