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

  val tuplesRDD = linesRDD
     .map(x => (x.split(",")(0), x.split(",")(1)))

//    val resultsBySexRDD = tuplesRDD
//    .map{case (a,b)  => if (b.contains(".")) (a, b.toDouble * 100) else (a, b.toDouble)}
//    .filter(_._2 > 0)
//      .aggregateByKey((0.0, 0))((acc, value) => (acc._1 + value, acc._2 +1 ), (acc1, acc2) => (acc1._1 + acc2._1, acc1._2+ acc2._2))
//      .map {case (sexo, (total, numero)) => (sexo, total / numero)}

  val resultsBySexRDD = tuplesRDD
    .map{case (a,b)  => if (b.contains(".")) (a, b.toDouble * 100) else (a, b.toDouble)}
    .filter(_._2 > 0)
    .groupByKey
    .map{case (sexo, iterator) => (sexo, iterator.sum / iterator.size)}


  resultsBySexRDD.saveAsTextFile("src/main/resources/alturas")





}
