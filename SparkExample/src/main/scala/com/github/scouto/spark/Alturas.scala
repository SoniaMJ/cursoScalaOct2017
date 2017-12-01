package com.github.scouto.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by scouto.
  */
object Alturas extends App {
  val conf: SparkConf = new SparkConf().setAppName("example").setMaster("local[*]").set("spark.hadoop.validateOutputSpecs", "false")

  val sc: SparkContext = new SparkContext(conf)

  val linesRDD:RDD[String] = sc.textFile("src/main/resources/alturas.csv")



}
