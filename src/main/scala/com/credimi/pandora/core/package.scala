package com.credimi.pandora

import com.credimi.pandora.core.annotations.Hash.merkle
import com.credimi.pandora.core.annotations.Ids
import com.credimi.pandora.core.annotations.Ids.ids
import com.credimi.pandora.core.annotations.Path.paths
import com.credimi.pandora.core.hash.HashFunction
import com.credimi.pandora.core.table.Table.UpsertTable
import com.credimi.pandora.core.table.Tables.UpsertTables
import com.credimi.pandora.core.table.{ Row, Table, Tables }
import com.credimi.pandora.core.write.Write

package object core {

  val singleValueFieldName = "value"

  import com.credimi.pandora.core.annotations.{ Hash, Path }

  private[core] def annotated[A, L: ByteSerializable](tree: Tree[A, L])(implicit
    hashFunction: HashFunction
  ): Tree[Ids[Path[Hash[A]]], L] =
    ids(paths(merkle(tree)))

  private[core] def writeStep[L](tables: Tables[L], write: Write[L]): Tables[L] =
    tables.upsert(write.tableId)(_.getOrElse(Table.empty).upsert(write.rowId)(Row.fromWrite(write)))

  def tables[A, L: ByteSerializable](tree: Tree[A, L], preserveListNodes: Boolean = false)(implicit
    hashFunction: HashFunction
  ): Tables[L] =
    annotated(tree).subtrees
      .flatMap(tree => Write.write(tree, preserveListNodes = preserveListNodes).toSeq)
      .foldLeft[Tables[L]](Tables.empty[L])(writeStep)
}
