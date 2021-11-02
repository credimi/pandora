package com.credimi.pandora.core.annotations

import com.credimi.pandora.core
import com.credimi.pandora.core.hash.HashFunction
import com.credimi.pandora.core.path.Step
import com.credimi.pandora.core.{ hash, ByteSerializable, Tree }

case class Ids[A](
  id: hash.Hash,
  parentId: Option[hash.Hash],
  nonListAncestor: Option[Ids.Ancestor],
  index: Option[Int],
  annotation: A
)

object Ids {

  case class Ancestor(
    path: core.path.Path,
    id: hash.Hash
  )

  def endOfPath(path: core.path.Path): Option[Step] =
    path.steps.lastOption

  def nodeId(parentId: Option[hash.Hash], path: core.path.Path, nodeHash: hash.Hash)(implicit
    hashFunction: HashFunction
  ): hash.Hash =
    hashFunction(
      parentId.toArray.flatMap(_.bytes) ++
        endOfPath(path).toArray.flatMap(implicitly[ByteSerializable[Step]].bytes) ++
        nodeHash.bytes
    )

  case class ParentData[A](annotation: Ids[Path[Hash[A]]], isList: Boolean)

  def ids[A, L](parentData: Option[ParentData[A]], annotation: Path[Hash[A]])(implicit
    hashFunction: HashFunction
  ): Ids[Path[Hash[A]]] = {
    val parentId: Option[hash.Hash] = parentData.map(_.annotation.id)
    val nonListParent: Option[Ancestor] =
      parentData.flatMap(parentData =>
        if (!parentData.isList) Some(Ancestor(parentData.annotation.annotation.path, parentData.annotation.id))
        else None
      )
    val nonListAncestor: Option[Ancestor] = nonListParent orElse parentData.flatMap(_.annotation.nonListAncestor)
    val path = annotation.path
    val nodeHash = annotation.annotation.hash
    Ids(
      id = nodeId(parentId = parentId, path = path, nodeHash = nodeHash),
      parentId = parentId,
      nonListAncestor = nonListAncestor,
      index = endOfPath(path).flatMap { case Step.Index(index) => Some(index); case _ => None },
      annotation = annotation
    )
  }

  def ids[A, L](
    parentData: Option[ParentData[A]]
  )(tree: Tree[Path[Hash[A]], L])(implicit hashFunction: HashFunction): Tree[Ids[Path[Hash[A]]], L] =
    tree match {
      case core.Leaf(annotation, leaf) =>
        core.Leaf(annotation = ids(parentData, annotation), leaf = leaf)
      case core.List(annotation, list) =>
        val nodeAnnotation = ids(parentData, annotation)
        core.List(
          annotation = nodeAnnotation,
          list = list.map(ids(Some(ParentData(annotation = nodeAnnotation, isList = true))))
        )
      case core.Dict(annotation, dict) =>
        val nodeAnnotation = ids(parentData, annotation)
        core.Dict(
          annotation = nodeAnnotation,
          dict = dict.view.mapValues(ids(Some(ParentData[A](annotation = nodeAnnotation, isList = false)))).toMap
        )
    }

  def ids[A, L](tree: Tree[Path[Hash[A]], L])(implicit hashFunction: HashFunction): Tree[Ids[Path[Hash[A]]], L] =
    ids(None)(tree)

}
