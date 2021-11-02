package com.credimi.pandora.core.write

import com.credimi.pandora.core
import com.credimi.pandora.core.hash.Hash
import com.credimi.pandora.core.path.Step
import com.credimi.pandora.core.table.TableId
import com.credimi.pandora.core.write.LeafWrite.{ ALeaf, NoLeaf }
import com.credimi.pandora.core.{ annotations, Leaf, Node, Tree }

case class Write[+L](
  tableId: TableId,
  rowId: Hash,
  parent: Option[Write.Parent],
  nodeHash: Option[Hash],
  leafWrite: LeafWrite[L]
)

object Write {

  case class Parent(tableId: TableId, rowId: core.hash.Hash)

  import annotations._

  def write[A, L](
    node: Tree[Ids[Path[core.annotations.Hash[A]]], L],
    preserveListNodes: Boolean
  ): Option[Write[L]] =
    node match {
      case Leaf(annotation, leaf) =>
        annotation.annotation.path.lastStep match {
          case None => None
          case Some(index: Step.Index) if preserveListNodes =>
            Some(
              Write(
                tableId = node.annotation.annotation.path, // <- a different table for each list leaf!
                rowId = node.annotation.id,
                parent = for {
                  path <- node.annotation.annotation.path.parentPath
                  id <- node.annotation.parentId
                } yield Parent(path, id),
                nodeHash = Some(node.annotation.annotation.annotation.hash),
                leafWrite = ALeaf(index, leaf)
              )
            )
          case Some(index: Step.Index) =>
            for {
              tableId <- node.annotation.annotation.path.parentPath
              parent <- node.annotation.nonListAncestor
            } yield Write(
              tableId = tableId, // <- add a record to the parent path
              rowId = node.annotation.id,
              parent = Some(Parent(parent.path, parent.id)),
              nodeHash = Some(node.annotation.annotation.annotation.hash),
              leafWrite = ALeaf(index, leaf)
            )
          case Some(field: Step.Field) =>
            for {
              tableId <- annotation.annotation.path.parentPath
              rowId <- if (preserveListNodes) annotation.parentId else annotation.nonListAncestor.map(_.id)
            } yield Write(
              tableId = tableId,
              rowId = rowId,
              parent = None, // to be written by the parent dict node
              nodeHash = None, // to be written by the parent dict node
              leafWrite = ALeaf(field, leaf)
            )
        }
      case (core.List(_, _)) if !preserveListNodes =>
        None
      case (node: Node[_, _]) if preserveListNodes =>
        Some(
          Write(
            tableId = node.annotation.annotation.path,
            rowId = node.annotation.id,
            parent = for {
              path <- node.annotation.annotation.path.parentPath
              id <- node.annotation.parentId
            } yield Parent(path, id),
            nodeHash = Some(node.annotation.annotation.annotation.hash),
            leafWrite = NoLeaf // to be written by the leaf nodes
          )
        )
      case (node: Node[_, _]) =>
        Some(
          Write(
            tableId = node.annotation.annotation.path,
            rowId = node.annotation.id,
            nodeHash = Some(node.annotation.annotation.annotation.hash),
            parent = for {
              path <- node.annotation.nonListAncestor.map(_.path)
              id <- node.annotation.nonListAncestor.map(_.id)
            } yield Parent(path, id),
            leafWrite = NoLeaf // to be written by the leaf nodes
          )
        )
    }
}
