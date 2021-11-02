package com.credimi.pandora.core.table

import com.credimi.pandora.core.utils.MapAlter

object Table {

  def empty[Leaf]: Table[Leaf] = Map.empty

  implicit class UpsertTable[L](table: Table[L]) {

    def upsert(rowId: RowId)(f: Option[Row[L]] => Row[L]): Table[L] =
      table.alter(rowId)(Some.apply[Row[L]] _ compose f)
  }
}
