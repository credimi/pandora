package com.credimi.pandora.core.annotations

import com.credimi.pandora.core
import com.credimi.pandora.core._
import com.credimi.pandora.core.path.Step

case class Path[A](path: core.path.Path, annotation: A)

object Path {

  def paths[A, L](tree: Tree[A, L]): Tree[Path[A], L] =
    tree match {
      case Leaf(annotation, leaf) =>
        Leaf(Path(core.path.Path.empty, annotation), leaf)
      case List(annotation, list) =>
        def f(child: Tree[Path[A], L], index: Int): Tree[Path[A], L] =
          child.mapAnnotation[Path[A]](annotation =>
            Path[A](Step.Index(index) +: annotation.path, annotation.annotation)
          )
        val pathsList: Seq[Tree[Path[A], L]] =
          list.map(paths[A, L]).zipWithIndex.map((f _).tupled)
        List(Path(core.path.Path.empty, annotation), pathsList)
      case Dict(annotation, dict) =>
        def f(field: String, child: Tree[Path[A], L]): (String, Tree[Path[A], L]) =
          (
            field,
            child.mapAnnotation(annotation => Path(Step.Field(field) +: annotation.path, annotation.annotation))
          )
        val pathsDict = dict.view
          .mapValues(paths[A, L])
          .map((f _).tupled)
          .toMap
        Dict(Path(core.path.Path.empty, annotation), pathsDict)
    }
}
