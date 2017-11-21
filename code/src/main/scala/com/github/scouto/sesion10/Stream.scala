package com.github.scouto.sesion10

import scala.annotation.tailrec
import Stream._
/**
  * Created by couto.
  */
sealed trait Stream[+A] {

  def headOption: Option[A] = {
    this match {
      case Empty => None
      case Cons(h, _) => Some(h())
    }
  }


  def toList: List[A] = {
    this match {
      case Empty => Nil
      case Cons(h, t) => h() :: t().toList
    }
  }

  @tailrec
  final def drop(n: Int): Stream[A] = {
    this match {
      case Cons(_, t) if n > 0 => t().drop(n - 1)
      case _ => this
    }
  }

  @tailrec
  final def dropWhile(f: A => Boolean): Stream[A] = {

    this match {
      case Cons (h, t) if f(h()) => t().dropWhile(f)
      case _ => this
    }

//    @tailrec
//    def go(rest: Stream[A]): Stream[A] = {
//      rest match {
//        case Empty => Empty
//        case Cons(h, t) if f(h()) => go(t())
//        case Cons(h, _) if !f(h()) => rest
//      }
//    }
//
//    go(this)
  }

  def take(n: Int): Stream[A] = {
    this match {
      case Cons(h, t) if n > 1 => cons(h(), t().take(n - 1))
      case Cons(h, _) if n == 1 => cons(h(), empty)
      case _ => empty
    }
  }

  def takeWhile(p: A => Boolean): Stream[A] = {
    this match {
      case Cons(h, t) if p(h()) => cons(h(), t().takeWhile(p))
      case _ => empty
    }
  }

  def exists(f: A => Boolean): Boolean = {
    this match {
      case Cons(h,t) => f(h()) || t().exists(f)
      case _ => false
    }
  }

  //Sesion 11
  def foldRight[B](z: => B)(f: (A, => B) => B): B = {
    this match {
      case Cons(h,t) => f(h(), t().foldRight(z)(f))
      case _ => z
    }
  }

  @tailrec
  final def foldLeft[B](z: => B)(f: ( => B, A) => B): B = {
    this match {
      case Cons(h,t) => t().foldLeft(f(z, h()))(f)
      case _ => z
    }
  }

  def existsFoldRight(f: A => Boolean): Boolean = {
    foldRight(false)((elem, acc) => f(elem) || acc)
  }

  def existsFoldLeft(f: A => Boolean): Boolean = {
    foldLeft(false)((acc, elem) => f(elem) || acc)
  }

  def forAll(p: A => Boolean): Boolean = {
//    foldRight(true)((elem, acc) => p(elem) && acc)
    foldLeft(true)((acc, elem) => p(elem) && acc)
  }


  def headOptionFold: Option[A] = {
      foldRight(None: Option[A])((elem, _) => Some(elem))
  }

  def takeWhileFold(p: A => Boolean): Stream[A] = {
      foldRight(empty[A])((elem, acc) => if (p(elem)) cons(elem, acc) else Empty)
  }

  def map[B](f: A => B): Stream[B] = {
    foldRight(empty[B])((elem, acc) => cons(f(elem), acc))
  }

  def filter(p: A => Boolean): Stream[A] = {
    foldRight(empty[A])((elem, acc) => if (p(elem)) cons(elem, acc) else acc)
  }


  def append[B >: A](other: => Stream[B]): Stream[B] = {
    foldRight(other)((elem, acc) => cons(elem, acc))
  }

  def flatMap[B](f: A => Stream[B]): Stream[B] = {
    foldRight(empty[B])((elem, acc) => f(elem) append acc )
  }

  def find(p: A => Boolean): Option[A] = {
      filter(p).headOption
  }
}
case object Empty extends Stream[Nothing]
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {

  //constructor
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl

    Cons(() => head, () => tail)
  }

  //constructor de empty Stream con tipo
  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] = {
    if (as.isEmpty) empty
    else cons(as.head, apply(as.tail: _*))
  }

  val ones: Stream[Int] = cons(1, ones)

  def constant[A](a: A): Stream[A] = {
    cons(a, constant(a))
  }

  def from(n: Int): Stream[Int] = {
    cons(n, from(n+1))
  }

  def fibs: Stream[BigDecimal] = {

    def loop(acc1: BigDecimal, acc2: BigDecimal) : Stream[BigDecimal]= {
      cons(acc1, loop(acc2, acc1+acc2))
    }
    loop(0,1)
  }

  def unfold[A,S](z: S)(f: S => Option[(A,S)]): Stream[A] = {
      f(z) match {
        case None => Empty
        case Some((value, state)) => cons(value, unfold(state)(f))
      }
  }

  def onesUnfold = {
      unfold(1)(_ => Some((1, 1)))
  }

  def constantUnfold[A](a: A): Stream[A] = {
    unfold(a)(_ => Some((a, a)))
  }

  def fromUnfold(n: Int): Stream[Int] = {
    unfold(n)(x => Some((x, x+1)))
  }

  def fibsUnfold: Stream[BigDecimal] = {
    unfold((0, 1)){case (acc1, acc2) => Some((acc1, (acc2, acc1+acc2)))}
  }

}


