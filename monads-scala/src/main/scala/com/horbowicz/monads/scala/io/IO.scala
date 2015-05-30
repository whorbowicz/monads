package com.horbowicz.monads.scala.io

final class IO[A](private val a: () => A) {
  def flatMap[B](f: A => IO[B]) = new IO(() => f(this.run()).run())

  def map[B](f: A => B): IO[B] = new IO(() => f(this.run()))

  def run(): A = a()
}

object IO {
  def apply[A](a: => A) = new IO(() => a)
}
