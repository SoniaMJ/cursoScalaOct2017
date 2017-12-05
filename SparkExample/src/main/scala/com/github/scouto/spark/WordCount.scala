package com.github.scouto.spark


import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by scouto.
  */
object WordCount extends App {
  val conf: SparkConf = new SparkConf().setAppName("example").setMaster("local[*]").set("spark.hadoop.validateOutputSpecs", "false")

  val sc: SparkContext = new SparkContext(conf)

  val linesRDD = sc.textFile("src/main/resources/Shakespeare.txt")

  val wordsRDD = linesRDD
    .flatMap(_.split("\\s"))
    .map(_.replaceAll("[^a-zA-Z0-9]", "").trim.toLowerCase)
    .filter(!_.isEmpty)
    .map(word => (word, 1))
    .reduceByKey(_+_)
    .sortBy(_._2, ascending = false)


  wordsRDD.repartition(5).saveAsTextFile("src/main/resources/wordCount")








}
