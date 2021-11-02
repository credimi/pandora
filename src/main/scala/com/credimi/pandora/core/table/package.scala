package com.credimi.pandora.core

import com.credimi.pandora.core.hash.Hash
import com.credimi.pandora.core.path.Path

package object table {

  type TableId = Path

  type RowId = Hash

  type Table[+Leaf] = Map[RowId, Row[Leaf]]

  type Tables[+Leaf] = Map[TableId, Table[Leaf]]
}
