package com.credimi.pandora.core.types

import com.credimi.pandora.core.DefaultHelpers.{ dict, int, list }
import com.credimi.pandora.core.annotations
import com.credimi.pandora.input.json.JsonLeaf
import org.specs2.mutable.Specification

object TreeTypeCheckingSpec {

  type A = Unit
  type L = JsonLeaf
  type LT = Unit

  implicit object leafTypeChecker extends LeafTypeChecking[L, LT] {
    override def check(term: L, `type`: LT): Either[LeafTypeCheckingError[L, LT], LT] = Right(`type`)
  }

  val leafTree = int(10)
  val leafTreeType = TreeType.Leaf[L, LT](())

  val listTree = list(leafTree, leafTree)
  val listTreeType = TreeType.List[L, LT](leafTreeType)

  val dictTree = dict("x" -> leafTree, "y" -> listTree)
  val dictTreeType = TreeType.Dict[L, LT](Map("x" -> leafTreeType, "y" -> listTreeType))
}

class TreeTypeCheckingSpec extends Specification {

  import TreeTypeCheckingSpec._

  for {
    castToList <- Seq(false, true)
    (name, tree, treeType) <- Seq(
      ("leaf", leafTree, leafTreeType),
      ("list", listTree, listTreeType),
      ("dict", dictTree, dictTreeType)
    )
  } s"(castToList = $castToList) ${name}Tree is a ${name}TreeType" >> {
    val treeWithPaths = annotations.Path.paths(tree)
    new TreeTypeChecking(castToList = castToList)
      .check[A, L, LT](
        treeType = treeType,
        tree = treeWithPaths
      ) must beRight(
      TreeTypeCheckingResult[A, L](Set.empty, treeWithPaths)
    )
  }

  for {
    castToList <- Seq(false)
    (treeName, treeTypeName, tree, treeType) <-
      (if (castToList)
         Seq.empty
       else Seq(("leaf", "list", leafTree, listTreeType))) ++ Seq(
        ("leaf", "dict", leafTree, dictTreeType),
        ("list", "leaf", listTree, leafTreeType),
        ("list", "dict", listTree, dictTreeType),
        ("dict", "leaf", dictTree, leafTreeType),
        ("dict", "list", dictTree, listTreeType)
      )
  } s"(castToList = $castToList) ${treeName}Tree is not a ${treeTypeName}TreeType" >> {
    val treeWithPaths = annotations.Path.paths(tree)
    new TreeTypeChecking(castToList = castToList)
      .check[A, L, LT](
        treeType = treeType,
        tree = treeWithPaths
      ) must beLeft
  }

  s"(castToList = true) check(List(dictTreeType), dictTree) must beRight" >> {
    val treeWithPaths = annotations.Path.paths(dictTree)
    new TreeTypeChecking(castToList = true)
      .check[A, L, LT](
        treeType = TreeType.List[L, LT](dictTreeType),
        tree = treeWithPaths
      ) must beRight
  }

  for {
    castToList <- Seq(false, true)
  } s"(castToList = $castToList) check(List(Bottom), List()) must beRight" >> {
    val treeWithPaths = annotations.Path.paths(list())
    new TreeTypeChecking(castToList = true)
      .check[A, L, LT](
        treeType = TreeType.List(TreeType.Bottom[L, LT]()),
        tree = treeWithPaths
      ) must beRight(
      TreeTypeCheckingResult[A, L](Set.empty, treeWithPaths)
    )
  }
}
