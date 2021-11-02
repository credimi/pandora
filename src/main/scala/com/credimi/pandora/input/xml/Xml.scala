package com.credimi.pandora.input.xml

import com.credimi.pandora.core.Tree
import com.credimi.pandora.input.TreeFormat
import org.json4s.{ JArray, JField, JObject, JString, JValue }

import scala.xml._

object Xml {

  def toJField(node: Node, leaf: Boolean = false): Option[JField] =
    node match {
      case (node: Elem) =>
        // We assume we don't have several attributes with the same name in an element, as per XML spec
        val nodeAttributes: Map[String, JString] =
          node.attributes.map((a: MetaData) => (a.key, JString(a.value.text))).toMap
        val leaf = node.child.forall(_.isAtom)
        val nodeChildrenByFieldName: Map[String, Seq[JValue]] =
          node.child.view.flatMap(toJField(_, leaf)).toSeq.groupBy(_._1).view.mapValues(_.map(_._2)).toMap
        val nodeChildren: Map[String, JValue] = nodeChildrenByFieldName.map {
          case (fieldName, Seq(value)) => (fieldName, value)
          case (fieldName, list)       => (fieldName, JArray(list.toList))
        }
        val fields: Map[String, JValue] = nodeAttributes ++ nodeChildren
        Some((node.label, JObject(fields.toSeq: _*)))
      case (_: PCData) =>
        None
      case (text: Text) =>
        if (leaf)
          Some("text", JString(text.text))
        else
          None
    }

  def toJObject(node: Node, leaf: Boolean = false): JObject = toJField(node, leaf).fold(JObject())(JObject(_))

  object TreeFormatXml extends TreeFormat[scala.xml.Node] {

    import com.credimi.pandora.input.json.Json._

    override type Leaf = TreeFormatJson.Leaf

    override def to(t: Node): Tree[Unit, Leaf] = {
      val jValue: JValue = toJObject(t)
      TreeFormatJson.to(jValue)
    }
  }
}
