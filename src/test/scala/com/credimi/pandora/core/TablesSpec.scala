package com.credimi.pandora.core

import com.credimi.pandora.core
import com.credimi.pandora.core.DefaultHelpers.{ dict, int, list, str }
import com.credimi.pandora.core.TablesSpec.{ dictOfListOfLists, listOfLists }
import com.credimi.pandora.core.hash.Hash
import com.credimi.pandora.core.path.Path
import com.credimi.pandora.core.path.Step.{ Field, Index }
import com.credimi.pandora.core.table.Row
import com.credimi.pandora.input.json.JsonLeaf
import org.specs2.mutable

object TablesSpec {

  val tree = dict("x" -> list(int(4), int(2), int(4)), "y" -> str("ciao"))
  val listOfLists = list(list(int(1)), list(int(2), int(3)))
  val dictOfListOfLists = dict("z" -> listOfLists)
}

class TablesSpec extends mutable.Specification {

  import DefaultHelpers._
  import com.credimi.pandora.core.TablesSpec.tree

  "core.tables(tree, preserveListNodes = false) creates the expected table" >> {
    core.tables(tree, preserveListNodes = false) must ===(
      Map(
        Path.make() -> Map(
          Hash(-58, 17) -> Row(
            parentTableId = None,
            parentId = None,
            nodeHash = Some(Hash(108, 107)),
            index = None,
            fields = Map("y" -> JsonLeaf.JString("ciao"))
          )
        ),
        Path.make(Field("x")) -> Map(
          Hash(44, -16) -> Row(
            parentTableId = Some(Path.make()),
            parentId = Some(Hash(-58, 17)),
            nodeHash = Some(Hash(-76, -39)),
            index = Some(0),
            fields = Map("value" -> JsonLeaf.JInt(4))
          ),
          Hash(41, -58) -> Row(
            parentTableId = Some(Path.make()),
            parentId = Some(Hash(-58, 17)),
            nodeHash = Some(Hash(111, -65)),
            index = Some(1),
            fields = Map("value" -> JsonLeaf.JInt(2))
          ),
          Hash(-59, -79) -> Row(
            parentTableId = Some(Path.make()),
            parentId = Some(Hash(-58, 17)),
            nodeHash = Some(Hash(-76, -39)),
            index = Some(2),
            fields = Map("value" -> JsonLeaf.JInt(4))
          )
        )
      )
    )
  }

  "core.tables(listOfLists, preserveListNodes = false) creates the expected table" >> {
    core.tables(listOfLists, preserveListNodes = false) must ===(
      Map(Path.make() -> Map.empty)
    )
  }

  "core.tables(dictOfListOfLists, preserveListNodes = false) creates the expected table" >> {
    core.tables(dictOfListOfLists, preserveListNodes = false) must ===(
      Map(
        Path.make() -> Map(
          Hash(114, 120) -> Row(
            parentTableId = None,
            parentId = None,
            nodeHash = Some(Hash(-67, -57)),
            index = None,
            fields = Map.empty
          )
        ),
        Path.make(Field("z"), Index(0)) -> Map(
          Hash(56, 102) -> Row(
            parentTableId = Some(Path.make()),
            parentId = Some(Hash(114, 120)),
            nodeHash = Some(Hash(-10, 120)),
            index = Some(0),
            fields = Map("value" -> JsonLeaf.JInt(1))
          )
        ),
        Path.make(Field("z"), Index(1)) -> Map(
          Hash(-20, 0) -> Row(
            parentTableId = Some(Path.make()),
            parentId = Some(Hash(114, 120)),
            nodeHash = Some(Hash(111, -65)),
            index = Some(0),
            fields = Map("value" -> JsonLeaf.JInt(2))
          ),
          Hash(48, 90) -> Row(
            parentTableId = Some(Path.make()),
            parentId = Some(Hash(114, 120)),
            nodeHash = Some(Hash(-31, 58)),
            index = Some(1),
            fields = Map("value" -> JsonLeaf.JInt(3))
          )
        )
      )
    )
  }

  "core.tables(tree, preserveListNodes = true) creates the expected table" >> {
    core.tables(tree, preserveListNodes = true) must ===(
      Map(
        Path.make() -> Map(
          Hash(-58, 17) -> Row(
            parentTableId = None,
            parentId = None,
            nodeHash = Some(Hash(108, 107)),
            index = None,
            fields = Map("y" -> JsonLeaf.JString("ciao"))
          )
        ),
        Path.make(Field("x")) -> Map(
          Hash(-43, 113) -> Row(
            parentTableId = Some(Path.make()),
            parentId = Some(Hash(-58, 17)),
            nodeHash = Some(Hash(-99, 87)),
            index = None,
            fields = Map()
          )
        ),
        Path.make(Field("x"), Index(0)) -> Map(
          Hash(44, -16) -> Row(
            parentTableId = Some(Path.make(Field("x"))),
            parentId = Some(Hash(-43, 113)),
            nodeHash = Some(Hash(-76, -39)),
            index = Some(0),
            fields = Map("value" -> JsonLeaf.JInt(4))
          )
        ),
        Path.make(Field("x"), Index(1)) -> Map(
          Hash(41, -58) -> Row(
            parentTableId = Some(Path.make(Field("x"))),
            parentId = Some(Hash(-43, 113)),
            nodeHash = Some(Hash(111, -65)),
            index = Some(1),
            fields = Map("value" -> JsonLeaf.JInt(2))
          )
        ),
        Path.make(Field("x"), Index(2)) -> Map(
          Hash(-59, -79) -> Row(
            parentTableId = Some(Path.make(Field("x"))),
            parentId = Some(Hash(-43, 113)),
            nodeHash = Some(Hash(-76, -39)),
            index = Some(2),
            fields = Map("value" -> JsonLeaf.JInt(4))
          )
        )
      )
    )
  }
}
