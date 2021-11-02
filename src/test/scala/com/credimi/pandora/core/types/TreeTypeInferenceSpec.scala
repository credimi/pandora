package com.credimi.pandora.core.types

import com.credimi.pandora.core
import com.credimi.pandora.input.json.JsonLeaf
import org.specs2.mutable.Specification

object TreeTypeInferenceSpec {

  type A = Unit
  type L = JsonLeaf
  type LT = Unit

  implicit object leafTypeInference extends LeafTypeInference[L, LT] {

    override def lub(left: LT, right: LT): Either[IncompatibleLeafTypes[L, LT], LT] =
      Right(())

    override def infer(term: L): Either[LeafTypeInferenceFailure[L, LT], LT] =
      Right(())
  }

  val leafTree = core.Leaf[A, L]((), JsonLeaf.JInt(10))
  val leafTreeType = TreeType.Leaf[L, LT](())

  val listTree = core.List[A, L]((), Seq(leafTree, leafTree))
  val listTreeType = TreeType.List[L, LT](leafTreeType)

  val dictTree = core.Dict[A, L]((), Map("x" -> leafTree, "y" -> listTree))
  val dictTreeType = TreeType.Dict[L, LT](Map("x" -> leafTreeType, "y" -> listTreeType))

  val inconsistentListTree1 = core.List[A, L]((), Seq(leafTree, listTree))
  val inconsistentListTree2 = core.List[A, L]((), Seq(leafTree, dictTree))
  val inconsistentListTree3 = core.List[A, L]((), Seq(listTree, dictTree))
}

class TreeTypeInferenceSpec extends Specification {

  import TreeTypeInferenceSpec._

  for {
    castToList <- Seq(false, true)
    (name, tree, treeType) <- Seq(
      ("leaf", leafTree, leafTreeType),
      ("list", listTree, listTreeType),
      ("dict", dictTree, dictTreeType)
    )
  } s"(castToList = $castToList) infer(${name}Tree) == Right(${name}TreeType)" >> {
    new TreeTypeInference(castToList).infer(tree) must beRight(treeType)
  }

  for {
    castToList <- Seq(false, true)
    listTree <-
      (if (castToList)
         Seq.empty
       else Seq(inconsistentListTree1)) ++ Seq(
        inconsistentListTree2,
        inconsistentListTree2
      )
  } s"(castToList = $castToList) infer($listTree) must beLeft" >> {
    new TreeTypeInference(castToList).infer(listTree) must beLeft
  }

}
