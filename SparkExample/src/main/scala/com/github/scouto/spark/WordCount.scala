package com.github.scouto.spark


import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by scouto.
  */
object WordCount extends App {
  val conf: SparkConf = new SparkConf().setAppName("example").setMaster("local[*]")

  val sc: SparkContext = new SparkContext(conf)

  val linesRDD = sc.textFile("src/main/resources/Shakespeare.txt")

  val rawWordsRDD = linesRDD.flatMap(_.split("\\s"))
    .map(_.replaceAll("[^a-zA-Z0-9]", "").trim.toLowerCase)

  val wordsRDD = rawWordsRDD.filter(!_.isEmpty)
    .map((_, 1))
    .reduceByKey(_ + _)
    .sortBy (_._2, ascending = false)


//  wordsRDD.take(10).foreach(println)

  wordsRDD.saveAsTextFile("src/main/resources/wordCount")

  //  val wordsRDD = rawWordsRDD.filter(!_.isEmpty)
//    .map((_, 1))
//    .reduceByKey(_ + _)
//    .map{case (a,b) => (b,a)}
//    wordsRDD.takeOrdered(10)(Ordering[(Int, String)].reverse)


//  Expected results
//  (the,27536)
//  (and,26664)
//  (i,20681)
//  (to,18876)
//  (of,18114)
//  (a,14561)
//  (you,13615)
//  (my,12481)
//  (that,11107)
//  (in,10948)

}
