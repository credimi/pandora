package com.credimi.pandora.core

package object rewriting {

  def flattenNestedDicts[L](sep: String)(tree: Tree[Unit, L]): Tree[Unit, L] =
    tree match {
      case leaf @ Leaf(_, _) => leaf
      case Dict(_, dict) =>
        val flattenedSubtree = dict.view.mapValues(flattenNestedDicts[L](sep)).toMap
        val childrenOfTypeDict = flattenedSubtree.collect { case (k, Dict(_, v)) => (k, v) }
        val flattenedSubtreeWithoutChildrenOfTypeDict =
          childrenOfTypeDict.keys.foldLeft(flattenedSubtree)(_ - _)
        val childrenOfTypeDictFlattened = for {
          (k1, v1) <- childrenOfTypeDict
          (k2, v2) <- v1
        } yield (s"$k1$sep$k2", v2)
        Dict((), flattenedSubtreeWithoutChildrenOfTypeDict ++ childrenOfTypeDictFlattened)
      case List(_, list) => List((), list.map(flattenNestedDicts(sep)))
    }
}
