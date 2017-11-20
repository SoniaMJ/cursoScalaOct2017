package com.github.scouto.sesion8


import scala.util.{Failure, Success, Try}

/**
  * Created by couto
  */
sealed trait Option[+A] {

  def map[B] (f: A => B): Option[B] = {
    this match {
      case None => None
      case Some(a) => Some(f(a))
    }
  }

  def getOrElse[B >: A] (default: => B): B = {
    this match {
      case None => default
      case Some(a) => a
    }

  }

  def flatMap[B](f: A => Option[B]) : Option[B] = {
    map(f) getOrElse None

//    this match {
//      case None => None
//      case Some(a) => f(a)
//    }

  }


  //devuelve el valor del Option si existe, en caso contrario devuelve el parámetro recibido
  def orElse[B >: A] (ob: => Option[B]): Option[B] = {
    map(Some(_)) getOrElse ob

//    this match {
//      case None => ob
//      case _ => this
//    }

  }

  def filter(f: A => Boolean): Option[A] = {
    flatMap(a => if (f(a)) Some(a) else None)

//    this match {
//      case Some(a) if f(a) => Some(a)
//      case _ => None
//    }
  }

}
case class Some[+A](get: A) extends Option[A]
case object None extends Option[Nothing]



object Option {


  def mean(xs:Seq[Double]): Option[Double] = {


    //sumar valores de lista => xs.sum
    //longitud de lista => xs.length
    //es vacia => xs.isEmpty


//    if (xs.isEmpty) None
//    else Some(xs.foldLeft(0.0)(_+_) / xs.length)

    if (xs.isEmpty) None
    else Some(xs.sum / xs.length)


  }

  def calcularCuotaString(age: String, incidencias: String): Option[Double] = {
    val myAge =  try {
      age.toInt
    }catch {
      case _: Exception => return None
    }

    val myInc =  try {
      incidencias.toInt
    }catch {
      case _: Exception => return None
    }

    Some(calcularCuota(myAge, myInc))
  }

  def calcularCuotaString2(age: String, incidencias: String): Option[Double] = {

    def intenta(value: String): Option[Int] = {
      Try(value.toInt) match {
        case Failure(_) => None
        case Success(r) => Some(r)
      }
    }

    map2(intenta(age), intenta(incidencias))(calcularCuota)
  }


    def calcularCuota(age: Int, incidencias: Int): Double = {
    age * incidencias
  }

  def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = {

    (a,b)  match {
      case (Some(v1), Some(v2)) => Some(f(v1, v2))
      case _ => None
    }

//    a flatMap(aa => b map (bb => f(aa, bb)))

//    for {
//      aa <- a
//      bb <- b
//    } yield f(aa, bb)

  }

  def sequence[A](a: List[Option[A]]): Option[List[A]] = {
    a.foldRight(Some(Nil): Option[List[A]])((elem, acc) => map2(elem,acc)(_::_))
  }

  def traverse[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] = {
    a.foldRight(Some(Nil): Option[List[B]])((elem, acc) => map2(f(elem),acc)(_::_))
  }

  def sequenceViaTraverse[A](a: List[Option[A]]): Option[List[A]] = {
      traverse(a)(x => x)
  }

  def variance(xs: Seq[Double]): Option[Double] = {
    mean(xs) flatMap(m => mean(xs.map(x => math.pow(x-m, 2))))
//    val m = mean(xs)
//    m match {
//      case None => None
//      case Some(media) => mean(xs.map(x => math.pow(x - media, 2)))
//    }
  }

}