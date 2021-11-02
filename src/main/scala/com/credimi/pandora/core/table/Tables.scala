package com.credimi.pandora.core.table

import com.credimi.pandora.core.path.Path
import com.credimi.pandora.core.utils.MapAlter

object Tables {

  def empty[Leaf]: Tables[Leaf] = Map[Path, Table[Leaf]](Path.empty -> Table.empty)

  implicit class UpsertTables[L](tables: Tables[L]) {

    def upsert(tableId: TableId)(f: Option[Table[L]] => Table[L]): Tables[L] =
      tables.alter(tableId)(Some.apply[Table[L]] _ compose f)
  }
}
