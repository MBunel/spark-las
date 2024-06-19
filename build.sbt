name := "spark-las"
organization := "com.github.mbunel"
version := "0.1.0"
scalaVersion := "2.13.11"

githubOwner := "mbunel"
githubRepository := "spark-las"
// See: https://github.com/target/data-validator/blob/98cf101659be553c3d929ace97f9c12ef191072d/build.sbt#L54
githubTokenSource := (TokenSource.Environment("GITHUB_TOKEN") ||
  TokenSource.GitConfig("github.token") ||
  TokenSource.Environment("SHELL"))

// Spark dependencies
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.1",
  "org.apache.spark" %% "spark-sql" % "3.5.1"
)

// LasZIP4J Backend
libraryDependencies += "com.github.mreutegg" % "laszip4j" % "0.17"
// libraryDependencies ++= Seq(
//   "io.pdal" %% "pdal" % "2.5.1", // core library
//   "io.pdal" % "pdal-native" % "2.5.1", // jni binaries
//   "io.pdal" %% "pdal-scala" % "2.5.1" // if scala core library (if required)
// )

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "latest.integration" % Test
)

//resolvers += Resolver.mavenLocal
publishMavenStyle := true
