import sbt._

object Dependencies {

  val scalatest = "org.scalatest" %% "scalatest" % "2.2.5"
  val scalamock = "org.scalamock" %% "scalamock-scalatest-support" % "3.2.2"

  val scalaReflect = "org.scala-lang" % "scala-reflect" % Settings.scalaVersion
  val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.0.3"

  val scalaResolvedConflicts = Seq(scalaReflect, scalaXml)

  val junit = "junit" % "junit" % "4.12"
  val junitInterface = "com.novocode" % "junit-interface" % "0.11"
  val mockito = "org.mockito" % "mockito-core" % "1.10.19"

  val scalaDependencies = Seq(scalatest % Test, scalamock % Test) ++ scalaResolvedConflicts
  val javaDependencies = Seq(junit % Test, junitInterface % Test, mockito % Test)

}