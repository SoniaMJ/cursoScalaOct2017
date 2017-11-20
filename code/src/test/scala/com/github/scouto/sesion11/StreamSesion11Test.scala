package com.github.scouto.sesion11

import com.github.scouto.sesion10.Stream
import org.scalacheck.Gen
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}

import scala.util.Try

/**
  * Created by couto on 30/06/17.
  */
class StreamSesion11Test extends FlatSpec with Matchers with PropertyChecks {

  val stream = Stream(1,2,3)
  val streamString = Stream("1","2","3")
  val size1Stream = Stream("Hello")
  val emptyStream = Stream()
  val emptyIntStream: Stream[Int] = Stream()

  "existsFoldRight" should "work as exists" in {
    emptyIntStream.existsFoldRight(_ > 5) should be (emptyIntStream.exists(_ > 5))
    stream.existsFoldRight(_ == 1) should be (stream.exists(_ == 1))
    stream.existsFoldRight(_ == 2) should be (stream.exists(_ == 2))
    stream.existsFoldRight(_ == 3) should be (stream.exists(_ == 3))
    stream.existsFoldRight(_ == 4) should be (stream.exists(_ == 4))
    stream.existsFoldRight(_ < 0) should be (stream.exists(_ < 0))
  }

  "existsFoldLeft" should "work as exists" in {
    emptyIntStream.existsFoldLeft(_ > 5) should be (emptyIntStream.exists(_ > 5))
    stream.existsFoldLeft(_ == 1) should be (stream.exists(_ == 1))
    stream.existsFoldLeft(_ == 2) should be (stream.exists(_ == 2))
    stream.existsFoldLeft(_ == 3) should be (stream.exists(_ == 3))
    stream.existsFoldLeft(_ == 4) should be (stream.exists(_ == 4))
    stream.existsFoldLeft(_ < 0) should be (stream.exists(_ < 0))
  }

  "forAll" should "return true if empty Stream" in {
    emptyIntStream.forAll(_ > 5) should be (true)
  }

  it should "return true if all the elements in the Stream satisfies the predicate" in {
    stream.forAll(_ > 0) should be (true)
    streamString.forAll(x => Try(x.toInt).isSuccess) should be (true)
  }
  
  it should "return false if at least one elements in the Stream does not satisfy the predicate" in {
    stream.forAll(_ > 1) should be (false)
    size1Stream.forAll(x => Try(x.toInt).isSuccess) should be (false)
  }

  "headOptionFold" should "work the same as headOption" in {
    emptyStream.headOptionFold should be (emptyStream.headOption)
    stream.headOptionFold should be (stream.headOption)

  }

  "takeWhileFold" should "work as takeWhile" in {
    emptyIntStream.takeWhileFold(_ > 5) should be (emptyIntStream.takeWhile(_ > 5))
    stream.takeWhileFold(_ < 3).toList should be (stream.takeWhile(_ < 3).toList)
    stream.takeWhileFold(_ %2 != 0).toList should be (stream.takeWhile(_ %2 != 0).toList)
    stream.takeWhileFold(_ < 10).toList should be (stream.takeWhile(_ < 10).toList)
    stream.takeWhileFold(_ < -10) should be (stream.takeWhile(_ < -10))
    stream.takeWhileFold(x => true).toList should be (stream.takeWhile(x => true).toList)
    stream.takeWhileFold(x => false) should be (stream.takeWhile(x => false))
  }

  "map" should "return empty for empty Stream" in {
    emptyIntStream.map(_ * 5) should be (Stream())
  }

  it should "return the Stream transformed for any other Stream" in {
    stream.map(_ * 5).toList should be (List(5, 10, 15))
    streamString.map(_.toInt).toList should be (List(1,2,3))
    size1Stream.map(_.length).toList should be (List(5))
  }

  "filter" should "be empty for empty streams" in {
    emptyIntStream.filter(_ > 1) should be (Stream())
  }

  it should "filter all elements that not satisfy the predicate" in {
    stream.filter(_ % 2 == 0).toList should be (List(2))
    stream.filter(_ % 2 != 0).toList should be (List(1,3))
  }

  "append" should "be the second stream for empty streams" in {
    emptyIntStream.append(stream).toList should be (stream.toList)
  }

  it should "be the first stream for empty second streams" in {
    stream.append(emptyIntStream).toList should be (stream.toList)

  }

  it should "be the appended stream in any other case" in {
    val s1: Stream[Int] = Stream(1)
    val s2: Stream[Int] = Stream(2,3,4)
    val s3: Stream[Int] = Stream(5, 6)
    s2.append(s1).toList should be (List(2,3,4, 1))
    s1.append(s3).toList should be (List(1,5, 6))
    s1.append(s2).append(s3).toList should be (List(1, 2,3,4,5, 6))

    val longStream: Stream[Long] = Stream(1, 2)
    val intStream: Stream[Int] = Stream(3, 4)

    longStream.append(intStream).toList should be (List(1,2,3,4))
    intStream.append(longStream).toList should be (List(3,4,1,2))

  }

  "flatMap" should "be empty for empty streams" in {
    emptyIntStream.flatMap(a => Stream(a, a)) should be (Stream())
  }

  it should "be the stream <<aplanada>>" in {
    stream.flatMap(a => Stream(a, a)).toList should be (List(1,1,2,2,3,3))
  }


  "find" should "return None for empty streams" in {
    emptyIntStream.find(_ > 1) should be (None)
  }

  it should "be the first element that satisfies the predicate" in {
    stream.find(_ % 2 == 0) should be (Some(2))
    stream.find(_ % 2 != 0) should be (Some(1))
  }



  "fibs" should "be the proper fibonacci sequence" in {



    val fibList = Stream.fibs.take(51).toList
    fibList.head should be (0)
    fibList(1) should be (1)
    fibList(2) should be (1)
    fibList(3) should be (2)
    fibList(4) should be (3)
    fibList(5) should be (5)
    fibList(6) should be (8)
    fibList(7) should be (13)
    fibList(8) should be (21)
    fibList(9) should be (34)

    val genPositiveInteger = for (n <- Gen.choose(0, 50)) yield n
    //Int.MaxValue = 2147483647
    //a partir e fib(47) se pasa de MaxInt
    forAll(genPositiveInteger) { (n: Int) =>
      if (n<2) fibList(n) should be (n)

      whenever(n >= 2) {
        println(s"fib(${n}) equals: ${fibList(n)}")
        fibList(n) should be (fibList(n-1) + fibList(n-2))
      }
    }
  }


























}
