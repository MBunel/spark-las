name := "spark-las"

version := "0.1.0"
scalaVersion := "2.13.11"
organization := "com.github.mbunel"

githubOwner := "MBunel"
githubRepository := "spark-las"

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