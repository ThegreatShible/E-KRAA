name := """E-KRAA"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)


scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice
libraryDependencies += javaJdbc



//JPA and hibernate

libraryDependencies ++= Seq(
  javaJpa, javaWs,ehcache,
  "org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final"
)

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test
libraryDependencies ++= Seq(
  "be.objectify" %% "deadbolt-java" % "2.6.1"
)


//postgres dependency
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.2"




// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))


