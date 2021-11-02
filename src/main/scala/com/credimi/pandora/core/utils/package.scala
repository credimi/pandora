package com.credimi.pandora.core

package object utils {

  implicit class MapAlter[K, V](map: Map[K, V]) {

    def alter(k: K)(f: Option[V] => Option[V]): Map[K, V] =
      f(map.get(k)) match {
        case None        => map - k
        case Some(value) => map.updated(k, value)
      }
  }

  implicit class TraverseSeq[X](seq: Seq[X]) {

    def traverse[A, B](f: X => Either[A, B]): Either[A, Seq[B]] =
      seq match {
        case Nil => Right[A, Seq[B]](Nil)
        case (x +: xs) =>
          for {
            y <- f(x)
            ys <- xs.traverse[A, B](f)
          } yield (y +: ys)
      }
  }
}
