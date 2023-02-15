package com
import scala.math.random
import org.apache.log4j._
import org.apache.spark.sql._
import java.lang.Thread
import org.apache.spark._
object SparkSampleJob {

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.DEBUG)
    val conf = new SparkConf()
      .setAppName("Spark Pi")
    val sc = new SparkContext(conf)
    val slices = if (args.length > 0) args(0).toInt else 2
    val n = math.min(100000L * slices, Int.MaxValue).toInt // avoid overflow
     println(s"Value of n is ${n}")
    val count = sc.parallelize(1 until n, slices).map { i =>
      val x = random * 2 - 1
      val y = random * 2 - 1
      if (x*x + y*y <= 1) 1 else 0
    }.reduce(_ + _)
    println(s"Pi is roughly ${4.0 * count / (n - 1)}")
    sc.stop()
  }
}
