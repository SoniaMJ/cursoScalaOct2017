package com.github.scouto.sesion4

import scala.annotation.tailrec

/**
  * Created by scouto.
  */
object Sesion4 extends App{


  def fib(n: Int): BigInt = {

    @tailrec
    def loop (acc1: BigInt, acc2: BigInt, x: Int) : BigInt = {
      if (x == n) acc1 + acc2
      else loop (acc2, acc1+ acc2, x+1)
    }

    if (n<2) n
    else loop (0, 1, 2)
  }

  def isSorted[A](as: List[A], f: (A,A) => Boolean): Boolean = {

    @tailrec
    def loop (rest: List[A]): Boolean = {
      rest match {
        case Nil => true
        case h::Nil => true
        case h1::t if f(h1, t.head) => loop (t)
        case h1::t if !f(h1, t.head) => false
      }
    }
    loop (as)
  }



  def msort2[T](less: (T, T) => Boolean, list: List[T]): List[T] = {


    def go(left: List[T], right: List[T]): List[T] = {
      (left, right) match {
        case (left, Nil) => left
        case (Nil, right) => right
        case (h1::t1, h2::t2) =>
          if (less(h1, h2)) h1::go(t1, right)
          else h2::go(left, t2)

      }
    }

    val n = list.length / 2
    if (n == 0) list
    else {
      val (l, r) = list.splitAt(n)
      go(msort2(less, l), msort2(less, r))
    }



  }


  def mSort3[T](less:(T,T)=>Boolean, l:List[T]) :List[T] = {
    val m = l.length / 2
    if (m == 0) l
    else {
      @tailrec
      def merge(ls: List[T], rs: List[T], acc: List[T] = List()): List[T] = (ls, rs) match {
        case (Nil, _) => acc ++ rs
        case (_, Nil) => acc ++ ls
        case (l :: ls1, r :: rs1) =>
          if (less(l, r)) merge(ls1, rs, acc :+ l)
          else merge(ls, rs1, acc :+ r)
      }
      val (a, b) = l splitAt m
      merge(mSort3(less,a), mSort3(less,b))
    }
  }

}
