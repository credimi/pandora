package com.credimi.pandora.input.xml

import com.credimi.pandora.core.{ Dict, Leaf, List, Tree }
import Xml.TreeFormatXml
import com.credimi.pandora.input.json.JsonLeaf
import org.specs2.mutable.Specification

import scala.collection.immutable
import scala.xml.{ Elem, NodeSeq }

class SameNameFieldsToArraySpec extends Specification {

  def elem(additionalNodes: NodeSeq): Elem =
    <A>
      ${additionalNodes}
      <B><C>1</C></B>
      <B><C>2</C></B>
      <B><C>3</C></B>
      <B><C>4</C></B>
    </A>

  val nodeSeq: NodeSeq =
    <C>1</C>
    <C>2</C>
    <C>3</C>
    <C>4</C>

  val treeSeq: immutable.Seq[Tree[Unit, JsonLeaf]] =
    nodeSeq.map(TreeFormatXml.to)

  "Tree with no additional elements contains all Bs" >> {

    TreeFormatXml.to(elem(NodeSeq.Empty)) must beLike { case Dict(_, dict) =>
      dict("A") must beLike { case Dict(_, dict) =>
        dict("B") must beLike { case List(_, list) =>
          list must ===(treeSeq)
        }
      }
    }

  }

  "Tree with additional D elements contains all Bs and additional element is translated as expected (DT-661)" >> {

    val additionalElement = <D>PASTICCIO CICCIO</D>

    TreeFormatXml.to(elem(additionalElement)) must beLike { case Dict(_, dict) =>
      dict("A") must beLike { case Dict(_, dict) =>
        dict("D") must ===(TreeFormatXml.to(additionalElement) match {
          case Dict(_, dict) => dict("D")
          case _             => Leaf((), JsonLeaf.JNull) // just to please exhaustiveness check
        }) and
          (dict("B") must beLike { case List(_, list) =>
            list must ===(treeSeq)
          })
      }
    }

  }
}
