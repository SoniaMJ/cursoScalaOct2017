name := "SparkExample"

version := "1.0"

scalaVersion := "2.11.7"


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.2.1",
  "junit" % "junit" % "4.10" % "test",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test",
  "org.apache.spark" %% "spark-sql" % "2.1.0"
)