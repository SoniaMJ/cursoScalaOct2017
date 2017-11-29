package com.github.scouto.sesion13

import com.github.scouto.sesion12.{RNG, State}
import com.github.scouto.sesion12.RNG.SimpleRNG
import org.scalacheck.Gen
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by scouto.
  */
class StateTest extends FlatSpec with Matchers with PropertyChecks {
  val genPositiveInteger = for (n <- Gen.choose(0, 5000)) yield n
  val simpleRNG = SimpleRNG(42)
  val rng = RNG

  "int" should "match the given size" in {
    val f = State.int.run(rng)
    f._1 shouldBe a [Int]
  }

//  "ints" should "match the given size" in {
//    val (l, r) = State.ints(5)
//    l.length should be (5)
//  }





}
