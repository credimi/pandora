package com.credimi.pandora.core

sealed trait Tree[+A, +L] {

  def annotation: A

  def mapAnnotation[B](f: A => B): Tree[B, L] =
    this match {
      case Leaf(annotation, leaf) => Leaf(f(annotation), leaf)
      case List(annotation, list) => List(f(annotation), list.map(_.mapAnnotation(f)))
      case Dict(annotation, dict) => Dict(f(annotation), dict.view.mapValues(_.mapAnnotation(f)).toMap)
    }

  lazy val subtrees: Seq[Tree[A, L]] =
    this match {
      case l @ Leaf(_, _)    => Seq(l)
      case l @ List(_, list) => l +: list.flatMap(_.subtrees)
      case d @ Dict(_, dict) => d +: dict.values.toSeq.flatMap(_.subtrees)
    }

  lazy val annotations: Seq[A] =
    subtrees.map(_.annotation)
}

final case class Leaf[A, L](annotation: A, leaf: L) extends Tree[A, L]

sealed trait Node[A, L] extends Tree[A, L]

final case class List[A, L](annotation: A, list: Seq[Tree[A, L]]) extends Node[A, L]

final case class Dict[A, L](annotation: A, dict: collection.immutable.Map[String, Tree[A, L]]) extends Node[A, L]
