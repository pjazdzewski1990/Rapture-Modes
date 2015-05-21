package io.scalac.blog.modes

import scala.reflect.ClassTag
import scala.util.Try


object Math {
  object WithTry {
    def divide(a: Int, b: Int) = Try{ a/b }
  }

  object WithModes {
    trait OurMode {
      type ResultWrapper[+_, _ <: Exception]
      def wrap[Result, ErrorResult <: Exception: ClassTag](body: => Result): ResultWrapper[Result, ErrorResult]
    }

    val tryMode = new OurMode {
      override type ResultWrapper[+T, _ <: Exception] = Try[T]
      override def wrap[
        Result,
        ErrorResult <: Exception : ClassTag]
      (body: => Result): ResultWrapper[Result, ErrorResult] = Try{ body }
    }

    val optionMode = new OurMode {
      override type ResultWrapper[+T, _ <: Exception] = Option[T]
      override def wrap[
        Result,ErrorResult <: Exception : ClassTag]
      (body: => Result): ResultWrapper[Result, ErrorResult] =
        try {
          Option(body)
        } catch {
          case e: Throwable =>
            println(s"DEBUG: Error during math ${e.getMessage}")
            None
        }
    }

    def divide(a: Int, b: Int, mode: OurMode): mode.ResultWrapper[Int, ArithmeticException] = mode.wrap {
      a/b
    }
  }
}
