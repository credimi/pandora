package com.credimi.pandora.core.types

import com.credimi.pandora.core
import com.credimi.pandora.core.annotations.Path
import com.credimi.pandora.core.utils._
import com.credimi.pandora.core.{ annotations, Tree }

sealed trait TreeTypeCheckingError[+A, L, LT]

case class LeafTypeCheckingError[L, LT](leaf: L, leafType: LT) extends TreeTypeCheckingError[Nothing, L, LT]

case class NodeTypeCheckingError[A, L, LT](treeType: TreeType[L, LT], tree: Tree[annotations.Path[A], L])
    extends TreeTypeCheckingError[A, L, LT]

trait LeafTypeChecking[X, XT] {

  def check(term: X, `type`: XT): Either[LeafTypeCheckingError[X, XT], Unit]
}

class TreeTypeChecking(castToList: Boolean) {

  import TreeType._

  def check[A, L, LT](treeType: TreeType[L, LT], tree: Tree[annotations.Path[A], L])(implicit
    leafTypeChecker: LeafTypeChecking[L, LT]
  ): Either[TreeTypeCheckingError[A, L, LT], TreeTypeCheckingResult[A, L]] =
    (treeType, tree) match {
      case (Leaf(leafType), t @ core.Leaf(_, leaf)) =>
        leafTypeChecker
          .check(leaf, leafType)
          .map(_ => TreeTypeCheckingResult[A, L](unexpectedSubtrees = Set.empty, checkedTree = t))
      case (List(Bottom()), t @ core.List(_, Seq())) =>
        Right(TreeTypeCheckingResult[A, L](unexpectedSubtrees = Set.empty, checkedTree = t))
      case (List(elementType), core.List(a, list)) =>
        list
          .traverse(check(elementType, _))
          .map(cs =>
            TreeTypeCheckingResult(
              unexpectedSubtrees = cs.map(_.unexpectedSubtrees).foldLeft(Set.empty[Tree[Path[A], L]])(_ ++ _),
              checkedTree = core.List(a, cs.map(_.checkedTree))
            )
          )
      case (List(elementType), notAList) if castToList =>
        // if we want a list but we have something else, just put it in a list!
        // NOTE that this might break the usefulness of the paths annotation and most probably also of the inner ones!
        check(List(elementType), core.List(notAList.annotation, Seq(notAList)))
      case (Dict(childrenTypes), core.Dict(a, dict)) =>
        // all fields are optional: we never enforce the actual presence of an expected field in a dict
        val results = dict.toSeq.traverse { case (k, t) =>
          childrenTypes.get(k) match {
            case None =>
              Right(Set(t), k -> None)
            case Some(schema) =>
              check(schema, t).map(checkOutput => (checkOutput.unexpectedSubtrees, k -> Some(checkOutput.checkedTree)))
          }
        }
        results.flatMap { checkOutputs =>
          val unexpectedSubtrees = checkOutputs.map(_._1).foldLeft(Set.empty[Tree[Path[A], L]])(_ ++ _)
          val checkedTree = core.Dict(a, checkOutputs.collect { case (_, (k, Some(t))) => (k, t) }.toMap)
          Right(TreeTypeCheckingResult(unexpectedSubtrees, checkedTree))
        }
      case (t, tree) => Left(NodeTypeCheckingError(t, tree))
    }
}
