package com.credimi.pandora.input.json

import com.credimi.pandora.core
import com.credimi.pandora.core.Tree
import com.credimi.pandora.input.TreeFormat
import org.json4s.{ JArray, JBool, JDecimal, JDouble, JInt, JLong, JNothing, JNull, JObject, JSet, JString, JValue }

case class Json4sSetUnsupported(values: Set[JValue]) extends Exception {
  val message = "Json4s sets not supported"
}

object Json {

  implicit object TreeFormatJson extends TreeFormat[JValue] {

    override type Leaf = JsonLeaf

    override def to(t: JValue): Tree[Unit, Leaf] =
      t match {
        case JNothing      => core.Leaf((), JsonLeaf.JNothing)
        case JNull         => core.Leaf((), JsonLeaf.JNull)
        case JString(s)    => core.Leaf((), JsonLeaf.JString(s))
        case JDouble(num)  => core.Leaf((), JsonLeaf.JDouble(num))
        case JDecimal(num) => core.Leaf((), JsonLeaf.JDecimal(num))
        case JLong(num)    => core.Leaf((), JsonLeaf.JLong(num))
        case JInt(num)     => core.Leaf((), JsonLeaf.JInt(num))
        case JBool(value)  => core.Leaf((), JsonLeaf.JBool(value))
        case JObject(obj)  => core.Dict((), obj.toMap.view.mapValues(to).toMap)
        case JArray(arr)   => core.List((), arr.map(to))
        case JSet(set)     => throw Json4sSetUnsupported(set)
      }

    def from[A](tree: Tree[A, Leaf]): JValue =
      tree match {
        case core.Leaf(_, leaf) =>
          leaf match {
            case JsonLeaf.JNothing        => org.json4s.JNothing
            case JsonLeaf.JNull           => org.json4s.JNull
            case JsonLeaf.JBool(value)    => org.json4s.JBool(value)
            case JsonLeaf.JDouble(value)  => org.json4s.JDouble(value)
            case JsonLeaf.JDecimal(value) => org.json4s.JDecimal(value)
            case JsonLeaf.JLong(value)    => org.json4s.JLong(value)
            case JsonLeaf.JInt(value)     => org.json4s.JInt(value)
            case JsonLeaf.JString(value)  => org.json4s.JString(value)
          }
        case core.List(_, list) => org.json4s.JArray(list.map(from).toList)
        case core.Dict(_, dict) => org.json4s.JObject(dict.view.mapValues(from).toList: _*)
      }
  }
}
