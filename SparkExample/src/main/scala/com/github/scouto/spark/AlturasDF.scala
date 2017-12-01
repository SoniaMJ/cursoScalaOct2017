package com.github.scouto.spark

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by scouto.
  */
object AlturasDF extends App {
  val conf: SparkConf = new SparkConf().setAppName("example").setMaster("local[*]").set("spark.hadoop.validateOutputSpecs", "false")
  val sc: SparkContext = new SparkContext(conf)
  val sqlContext = new org.apache.spark.sql.SQLContext(sc)

  lazy val ss:SparkSession = SparkSession
    .builder()
    .master("local[6]")
    .appName(this.getClass.getSimpleName).getOrCreate()













}
