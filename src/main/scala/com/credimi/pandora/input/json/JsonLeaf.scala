package com.credimi.pandora.input.json

import com.credimi.pandora.core.ByteSerializable
import org.json4s.JsonAST.JString
import org.json4s.native.Serialization
import org.json4s.{ DefaultFormats, JBool, JDecimal, JDouble, JInt, JLong, JNothing, JNull, JValue }

sealed trait JsonLeaf {

  type Value

  def value: Value

  def jValue: JValue =
    this match {
      case JsonLeaf.JNothing        => JNothing
      case JsonLeaf.JNull           => JNull
      case JsonLeaf.JBool(value)    => JBool(value)
      case JsonLeaf.JDouble(value)  => JDouble(value)
      case JsonLeaf.JDecimal(value) => JDecimal(value)
      case JsonLeaf.JLong(value)    => JLong(value)
      case JsonLeaf.JInt(value)     => JInt(value)
      case JsonLeaf.JString(value)  => JString(value)
    }

}

object JsonLeaf {

  case object JNothing extends JsonLeaf {
    type Value = None.type
    def value = None
  }

  case object JNull extends JsonLeaf {
    type Value = Null
    def value = null
  }

  case class JBool(value: Boolean) extends JsonLeaf {
    type Value = Boolean
  }

  trait JNumber

  case class JDouble(value: Double) extends JsonLeaf with JNumber {
    type Value = Double
  }

  case class JDecimal(value: BigDecimal) extends JsonLeaf with JNumber {
    type Value = BigDecimal
  }

  case class JLong(value: Long) extends JsonLeaf with JNumber {
    type Value = Long
  }

  case class JInt(value: BigInt) extends JsonLeaf with JNumber {
    type Value = BigInt
  }

  case class JString(value: String) extends JsonLeaf {
    type Value = String
  }

  implicit val byteSerializableJsonLeaf: ByteSerializable[JsonLeaf] = { jsonLeaf =>
    Serialization.write(jsonLeaf.value)(DefaultFormats).getBytes("UTF-8")
  }
}
