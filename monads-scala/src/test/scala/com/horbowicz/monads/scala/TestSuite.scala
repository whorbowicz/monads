package com.horbowicz.monads.scala

import org.scalamock.matchers.Matchers
import org.scalamock.scalatest.MockFactory
import org.scalatest.FreeSpec

trait TestSuite extends FreeSpec with MockFactory with Matchers
