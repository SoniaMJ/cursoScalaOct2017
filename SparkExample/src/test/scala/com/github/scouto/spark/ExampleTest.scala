package com.github.scouto.spark

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, FunSuite}
/**
  * Created by scouto.
  */


@RunWith(classOf[JUnitRunner])
class ExampleSuite extends FunSuite with BeforeAndAfterAll {

  def initializeExample(): Boolean =
    try {
      Example
      true
    } catch {
      case _: Throwable => false
    }

  override def afterAll(): Unit = {
    assert(initializeExample(), " -- did you fill in all the values in Example (conf, sc)?")
    import Example._
    sc.stop()
  }


  test("'sumOfPlusOnes List(1, 2, 3, 4, 5)' should be equal to 20") {

    assert(initializeExample(), " -- did you fill in all the values in Example (conf, sc)?")
    import Example._
    assert(sumOfPlusOnes == 20)
  }

}
