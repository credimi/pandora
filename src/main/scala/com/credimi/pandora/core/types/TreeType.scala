package com.credimi.pandora.core.types

import com.credimi.pandora.core._

sealed trait TreeType[+L, +LT]

case class TreeTypeCheckingResult[A, L](
  unexpectedSubtrees: Set[Tree[annotations.Path[A], L]],
  checkedTree: Tree[annotations.Path[A], L]
)

object TreeType {

  case class Bottom[L, LT]() extends TreeType[L, LT]
  case class Leaf[L, LT](leafType: LT) extends TreeType[L, LT]
  case class List[L, LT](elementType: TreeType[L, LT]) extends TreeType[L, LT]
  case class Dict[L, LT](childrenType: Map[String, TreeType[L, LT]]) extends TreeType[L, LT]
}
