package com.credimi.pandora.core.table

import com.credimi.pandora.core.hash.Hash
import com.credimi.pandora.core.path.Step
import com.credimi.pandora.core.singleValueFieldName
import com.credimi.pandora.core.write.LeafWrite.{ ALeaf, NoLeaf }
import com.credimi.pandora.core.write.{ LeafWrite, Write }

case class Row[+Leaf](
  parentTableId: Option[TableId],
  parentId: Option[Hash],
  nodeHash: Option[Hash],
  index: Option[Int],
  fields: Map[String, Leaf]
)

object Row {

  def fromLeafWrite[L](leafWrite: LeafWrite[L]): Map[String, L] = leafWrite match {
    case ALeaf(Step.Index(_), leaf)     => Map(singleValueFieldName -> leaf)
    case ALeaf(Step.Field(field), leaf) => Map(field -> leaf)
    case NoLeaf                         => Map.empty[String, L]
  }

  def fromWrite[L](write: Write[L]): Option[Row[L]] => Row[L] = {
    case None =>
      Row[L](
        parentTableId = write.parent.map(_.tableId),
        parentId = write.parent.map(_.rowId),
        nodeHash = write.nodeHash,
        index = write.leafWrite.toIndex,
        fields = fromLeafWrite(write.leafWrite)
      )
    case Some(existingRow) =>
      existingRow.copy(
        parentId = existingRow.parentId orElse write.parent.map(_.rowId),
        nodeHash = existingRow.nodeHash orElse write.nodeHash,
        index = existingRow.index orElse write.leafWrite.toIndex,
        fields = existingRow.fields ++ fromLeafWrite[L](write.leafWrite)
      )
  }

}
