name := """E-KRAA"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)


scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice
libraryDependencies += javaJdbc

// Test Database
libraryDependencies += "com.h2database" % "h2" % "1.4.196"

//JPA and hibernate

libraryDependencies ++= Seq(
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final" // replace by your jpa implementation
)

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test
libraryDependencies ++= Seq(
  "be.objectify" %% "deadbolt-java" % "2.6.1"
)


//postgres dependency
libraryDependencies += "postgresql" % "postgresql" % "9.1-901-1.jdbc4"



// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))


