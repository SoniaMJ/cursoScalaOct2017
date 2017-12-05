package com.github.scouto.spark

import org.apache.spark.sql.{SparkSession, functions}
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

  val mToCM = (s:String) => {
    if (s.contains("."))
      s.toDouble * 100
    else s.toDouble
  }

  val mToCMudf = functions.udf(mToCM)

  val rawDF = ss
    .read
    .csv("src/main/resources/alturas.csv")
    .toDF(List("sexo", "altura"): _*)
    .where("altura is not null and altura > 0")


    val myDataFrame = rawDF
    .withColumn("altura", mToCMudf(rawDF("altura")))
      .createOrReplaceTempView("tmpTable")

    val avgDF = ss.sql("select sexo, avg(altura) as altura from tmpTable group by sexo")


    avgDF.printSchema()
    avgDF.show()
  avgDF.coalesce(1).rdd.saveAsTextFile("src/main/resources/alturasDF")











}
