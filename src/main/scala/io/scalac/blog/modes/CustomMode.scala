package io.scalac.blog.modes

import rapture.core.{ModeGroup, Mode}
import rapture.data.ParseMethods

import scala.reflect.ClassTag

object CustomMode {
  implicit val seqMode = new Mode[ParseMethods] {
    type Wrap[+T, E <: Exception] = Seq[T]
    override def wrap[T, E <: Exception: ClassTag](t: => T): Seq[T] =
      try Seq(t) catch {
        case e: Exception => Seq.empty[T]
      }

    protected def unwrap[Return](value: Wrap[Return, _ <: Exception]): Return = value.head
  }
}
