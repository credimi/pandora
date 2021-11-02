package com.credimi.pandora.core.types

import com.credimi.pandora.core
import com.credimi.pandora.core.Tree
import com.credimi.pandora.core.utils.TraverseSeq

sealed trait TreeTypeInferenceFailure[+L, +LT]

case class IncompatibleLeafTypes[+L, +LT](left: L, right: L) extends TreeTypeInferenceFailure[L, LT]

case class IncompatibleTreeTypes[+L, +LT](left: TreeType[L, LT], right: TreeType[L, LT])
    extends TreeTypeInferenceFailure[L, LT]
case class LeafTypeInferenceFailure[+L, +LT](term: L) extends TreeTypeInferenceFailure[L, LT]

trait LeafTypeInference[L, LT] {

  def lub(left: LT, right: LT): Either[IncompatibleLeafTypes[L, LT], LT]
  def infer(term: L): Either[LeafTypeInferenceFailure[L, LT], LT]
}

class TreeTypeInference(castToList: Boolean) {

  def lub[L, LT](
    left: TreeType[L, LT],
    right: TreeType[L, LT]
  )(implicit leafTypeInference: LeafTypeInference[L, LT]): Either[TreeTypeInferenceFailure[L, LT], TreeType[L, LT]] =
    (left, right) match {
      case (TreeType.Bottom(), t)                         => Right(t)
      case (t, TreeType.Bottom())                         => Right(t)
      case (TreeType.Leaf(left), TreeType.Leaf(right))    => leafTypeInference.lub(left, right).map(TreeType.Leaf.apply)
      case (TreeType.List(left), TreeType.List(right))    => lub(left, right).map(TreeType.List.apply)
      case (left @ TreeType.List(_), right) if castToList => lub(left, TreeType.List(right))
      case (left, right @ TreeType.List(_)) if castToList => lub(TreeType.List(left), right)
      case (TreeType.Dict(left), TreeType.Dict(right)) =>
        val commonKeys = left.keySet.intersect(right.keySet)
        commonKeys.toSeq
          .traverse(key => lub(left(key), (right(key))).map((key, _)))
          .map(commonPairs => (left -- commonKeys) ++ commonPairs.toMap ++ (right -- commonKeys))
          .map(pairs => TreeType.Dict(pairs))
      case (left, right) => Left(IncompatibleTreeTypes(left, right))
    }

  def infer[A, L, LT](
    tree: Tree[A, L]
  )(implicit leafTypeInference: LeafTypeInference[L, LT]): Either[TreeTypeInferenceFailure[L, LT], TreeType[L, LT]] =
    tree match {
      case core.Leaf(_, leaf) => leafTypeInference.infer(leaf).map(TreeType.Leaf.apply)
      case core.List(_, list) =>
        type Left = TreeTypeInferenceFailure[L, LT]
        type Right = TreeType[L, LT]
        list
          .traverse[Left, Right](infer[A, L, LT])
          .flatMap(
            _.foldRight[Either[Left, Right]](Right(TreeType.Bottom[L, LT]())) {
              case (x: Right, acc: Either[Left, Right]) =>
                acc.flatMap(lub[L, LT](x, _))
            }
          )
          .map(TreeType.List.apply)
      case core.Dict(_, dict) =>
        dict.toSeq.traverse { case (key, value) => infer(value).map((key, _)) }
          .map(seq => TreeType.Dict(seq.toMap))

    }
}
