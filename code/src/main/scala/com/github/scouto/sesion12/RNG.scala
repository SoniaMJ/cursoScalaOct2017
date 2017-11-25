package com.github.scouto.sesion12

import scala.annotation.tailrec

/**
  * Created by couto on 5/07/17.
  */

trait RNG {
  def nextInt: (Int, RNG)
}


object RNG {

  case class SimpleRNG(seed: Long) extends RNG {
    def nextInt: (Int, RNG) = {
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL // `&` is bitwise AND. We use the current seed to generate a new seed.
      val nextRNG = SimpleRNG(newSeed) // The next state, which is an `RNG` instance created from the new seed.
      val n = (newSeed >>> 16).toInt // `>>>` is right binary shift with zero fill. The value `n` is our new pseudo-random integer.
      (n, nextRNG) // The return value is a tuple containing both a pseudo-random integer and the next `RNG` state.
    }
  }


  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    rng.nextInt match {
      case (Integer.MIN_VALUE, newRNG) => (0, newRNG)
      case (x, newRNG) if x < 0=> (Math.abs(x), newRNG)
      case (x, newRNG) => (x, newRNG)
    }
  }

  def double(rng: RNG): (Double, RNG) = {
    val (i, r) = nonNegativeInt(rng)
    (i / (Integer.MAX_VALUE.toDouble + 1), r)
  }


  def intDouble(rng: RNG): ((Int, Double), RNG) = {
    val (i, r1) = rng.nextInt
    val (d, r2) = double(r1)
    ((i,d), r2)
  }

  def doubleInt(rng: RNG): ((Double, Int), RNG) = {
    val ((i, d), r1) = intDouble(rng)
    ((d, i), r1)
  }

  def double3(rng: RNG): ((Double, Double, Double), RNG) = {
    val (d1, r1) = double(rng)
    val (d2, r2) = double(r1)
    val (d3, r3) = double(r2)
    ((d1,d2,d3), r3)
  }

  def ints(n: Int)(rng: RNG): (List[Int], RNG) = {
    @tailrec
    def loop(acc: List[Int], currentCount: Int)(currentRng: RNG): (List[Int], RNG) = {

      currentCount match {
        case x if x <= 0 => (acc, currentRng)
        case x if x > 0 => {
          val (i, newRNG) = currentRng.nextInt
          loop(i::acc, x-1)(newRNG)
        }
      }
    }
    loop(Nil, n)(rng)
  }


  type Rand[+A] = RNG => (A, RNG)

  val int: Rand[Int] = _.nextInt

  def unit[A](a: A): Rand[A] = rng => (a, rng)

  def map[A,B](s:Rand[A])(f: A => B): Rand[B] = {
    rng => {
      val (a, rng2) = s(rng)
      (f(a), rng2)
    }
  }

  def nonNegativeEven: Rand[Int] = {
    map(nonNegativeInt)(x => x - x % 2)
  }

  def doubleMap: Rand[Double] = {
    map(nonNegativeInt)(x => x / (Integer.MAX_VALUE.toDouble + 1))
  }

  def map2[A,B, C](ra:Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = {
    rng => {
      val (a, rng2) = ra(rng)
      val (b, rng3) = rb(rng2)
      (f(a, b), rng3)
    }
  }

  def  both[A, B](ra:Rand[A], rb: Rand[B]): Rand[(A,B)] = {
//    map2(ra, rb)((a,b) => (a,b))
    map2(ra, rb)((_,_))
  }

  def intDoubleBoth: Rand[(Int, Double)] = {
    both(int, double)
  }

  def doubleIntBoth: Rand[(Double, Int)] = {
    both(double, int)
  }




}








