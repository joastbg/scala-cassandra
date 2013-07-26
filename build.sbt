organization := "net.fsenterprise"

version := "0.1"

scalaVersion := "2.10.0"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Twitter's Repository" at "http://maven.twttr.com/"
)

libraryDependencies ++= Seq(
  "me.prettyprint" 		 % "hector-core" 			% "1.0-5",
  "me.prettyprint" 		 % "hector-object-mapper" 	% "1.0-05",
  "javax.persistence" 	 % "persistence-api" 		% "1.0"
)

parallelExecution in Test := false
