package com.github.scouto.spark


import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by scouto.
  */
object WordCount extends App {
  val conf: SparkConf = new SparkConf().setAppName("example").setMaster("local[*]")

  val sc: SparkContext = new SparkContext(conf)

  val linesRDD = sc.textFile("src/main/resources/Shakespeare.txt")

}
