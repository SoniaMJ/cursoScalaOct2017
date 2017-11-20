package com.github.scouto.sesion8

/**
  * Created by scouto.
  */
object Sesion8 extends App{

  val xs = List(1,2,3)
  val ys = List("a", "b", "c")

  val mapped = for {
    x <- xs
  } yield x+1

  println("mapped: " +mapped)

  val filtered = for  {
    x <- xs
    if x % 2 == 0
  } yield x

  println("filtered: " + filtered)


  val filteredMapped = for {
    x <- xs
    if x % 2 == 0
  } yield x + 1

  println("filteredMapped: " + filtered)




  xs.flatMap(x => ys.map(y => (x, y)))

  val flatMapped = for {
    x <- xs
    y <- ys
  } yield (x,y)

  println("flatMapped: " + flatMapped)

  val all = for {
    x <- xs if x % 2 == 0
    y <- ys
  } yield (x, y)

  println("all: " + all)




  val lista = List("My", "taylor", "is","really", "rich", "and", "you", "are", "not")
  val listaDeListas = List(List("My"), List("taylor", "is"),List("really", "rich", "and"), List("you"), List("are", "not"))





  //Ejercicio 1
  val ejer1 =
    for {
      word <- lista
    } yield word.length

  assert(ejer1 == List(2, 6, 2,6, 4,3,3,3,3))

  //Ejercicio 2
  val ejer2 =
    for {
      word <- lista
      if word.length % 2 != 0
    } yield (word, word.length)

  assert(ejer2 == List(("and", 3),("you", 3),("are",3),("not",3)))


  //Ejercicio 3
  val ejer3 =
    for {
      list <- listaDeListas
      word <- list if list.length > 2
      if word.length % 2 != 0
    } yield (word, word.length)

  assert(ejer3 == List(("and", 3)))
}
