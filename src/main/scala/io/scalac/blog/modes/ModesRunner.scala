package io.scalac.blog.modes

import java.io.File
import java.nio.file.Path

import rapture.core._
import rapture.json._
import rapture.json.jsonBackends.play._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

object ModesRunner {

  def parseAsValueOrException(s: String) = {
    import modes.throwExceptions._
    Json.parse(s)
  }
  def parseAsEither(s: String) = {
    import modes.returnEither._
    Json.parse(s)
  }
  def parseAsOption(s: String) = {
    import modes.returnOption._
    Json.parse(s)
  }
  def parseAsTry(s: String) = {
    import modes.returnTry._
    Json.parse(s)
  }
  def parseAsFuture(s: String) = {
    import modes.returnFuture._
    Json.parse(s)
  }
  def parseAsJava(s: String) = {
    import modes.keepCalmAndCarryOn._
    Json.parse(s)
  }
  def parseAsCustom(s: String) = {
    import CustomMode._
    Json.parse(s)
  }

  def wrappedLegacy() = {
    import modes.returnOption._
    modally { List().head }
  }

  def main (args: Array[String]): Unit = {
    def jsonString = "[1, 2, 3]"
    def errorString = "]["

    println("Value or Exception: ")
    println(parseAsValueOrException(jsonString))
    Try{ parseAsValueOrException(errorString) }.recover {
      case err:Throwable => println(s"Exception '$err' was thrown")
    }

    println("\nEither: ")
    println(parseAsEither(jsonString))
    println(parseAsEither(errorString))

    println("\nOption: ")
    println(parseAsOption(jsonString))
    println(parseAsOption(errorString))

    println("\nTry: ")
    println(parseAsTry(jsonString))
    println(parseAsTry(errorString))

    println("\nFuture: ")
    parseAsFuture(jsonString).onSuccess {case x => println(s"Future ended with '$x'"); x }
    parseAsFuture(errorString).onFailure {case x: Throwable => println(s"Future failed with '$x'"); x }

    println("\nJava Style: ")
    println(parseAsJava(jsonString))
    println(parseAsJava(errorString))

    println("\nCustom Mode: ")
    println(parseAsCustom(jsonString))
    println(parseAsCustom(errorString))

    println("\nMath Test: ")
    println(Math.WithTry.divide(1, 4))

    println(Math.WithModes.divide(1, 4, Math.WithModes.tryMode))
    println(Math.WithModes.divide(1, 0, Math.WithModes.tryMode))
    println(Math.WithModes.divide(1, 4, Math.WithModes.optionMode))
    println(Math.WithModes.divide(1, 0, Math.WithModes.optionMode))

    println("Wrapped legacy code: " + wrappedLegacy())
  }
}
