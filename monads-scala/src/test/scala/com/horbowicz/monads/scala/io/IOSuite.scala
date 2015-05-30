package com.horbowicz.monads.scala.io

import com.horbowicz.monads.scala.TestSuite
import org.scalatest.Matchers._

class IOSuite extends TestSuite {

  val Input: String = "TestInput"
  val Result: Int = 42

  "An IO" - {
    "when created" - {
      "should not execute passed operation" in {
        val operation = mock[() => Unit]
        operation.apply _ expects() never()

        IO(operation())
      }
    }

    "when run is called" - {
      "should perform it's operation once" in {
        val operation = mock[() => Unit]
        operation.apply _ expects() once()

        IO(operation()).run()
      }
    }

    "when run is called" - {
      "should return operation's result" in {
        val inputOperation = mock[() => String]
        inputOperation.apply _ expects() once() returning Input

        val actual = IO(inputOperation()).run()

        actual shouldEqual Input
      }
    }

    "when map is called" - {
      "should not execute passed function" in {
        val operation = mock[() => Unit]
        val secondOperation = mock[() => Unit]
        operation.apply _ expects() never()
        secondOperation.apply _ expects() never()

        IO(operation()).map(_ => secondOperation())
      }
    }

    "when run is called after map" - {
      "should execute operation followed by passed function" in {
        val operation = mock[() => Unit]
        val function = mock[Unit => Unit]
        inSequence {
          operation.apply _ expects() once()
          function.apply _ expects * once()
        }

        IO(operation()).map(function).run()
      }
    }

    "when run is called after map" - {
      "should pass result of first operations as input to map's function" in {
        val inputOperation = mock[() => String]
        inputOperation.apply _ expects() once() returning Input

        IO(inputOperation()).map(actual =>
          actual shouldEqual Input
        ).run()
      }
    }

    "when run is called after map" - {
      "should return result map's function" in {
        val inputOperation = mock[() => Unit]
        val function = mock[Unit => Int]
        inputOperation.apply _ expects() once()
        function.apply _ expects * once() returning Result

        val actualResult = IO(inputOperation()).map(function).run()

        actualResult shouldEqual Result
      }
    }

    "when flatMap is called" - {
      "should not execute passed function" in {
        val operation = mock[() => Unit]
        val function = mock[Unit => IO[Unit]]
        operation.apply _ expects() never()
        function.apply _ expects * never()

        IO(operation()).flatMap(function)
      }
    }

    "when run is called after flatMap" - {
      "should execute in order" +
        "initial operation" +
        "flatMap's argument function and " +
        "operation of flatMap's argument function result" in {
        val operation = mock[() => Unit]
        val secondOperation = mock[() => Unit]
        val function = mock[Unit => IO[Unit]]
        inSequence {
          operation.apply _ expects() once()
          function.apply _ expects * once() returning IO(secondOperation())
          secondOperation.apply _ expects() once()
        }

        IO(operation()).flatMap(function).run()
      }
    }

    "when run is called after flatMap" - {
      "should pass result of first operation " +
        " as input to flatMap's argument" in {
        val inputOperation = mock[() => String]
        inputOperation.apply _ expects() once() returning Input

        IO(inputOperation()).flatMap(actual =>
          IO(actual shouldEqual Input)
        ).run()
      }
    }

    "when run is called after flatMap" - {
      "should return result of operation " +
        "that was returned from flatMap's argument function" in {
        val operation = mock[() => Unit]
        val secondOperation = mock[() => Int]
        val function = mock[Unit => IO[Int]]
        inSequence {
          operation.apply _ expects() once()
          function.apply _ expects * once() returning IO(secondOperation())
          secondOperation.apply _ expects() once() returning Result
        }

        val actualResult = IO(operation()).flatMap(function).run()

        actualResult shouldEqual Result
      }
    }
  }
}
