package com.github.scouto.spark


import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by scouto.
  */
object Example{
  val conf: SparkConf = new SparkConf().setAppName("example").setMaster("local[*]")

  val sc: SparkContext = new SparkContext(conf)

  val myFirstRDD: RDD[Int] = sc.parallelize(List(1, 2, 3, 4, 5))

  def sumOfPlusOnes = myFirstRDD.map(_+1).reduce(_+_)

  println(sumOfPlusOnes)
}
