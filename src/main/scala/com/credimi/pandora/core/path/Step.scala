package com.credimi.pandora.core.path

import com.credimi.pandora.core.ByteSerializable

import java.math.BigInteger

sealed trait Step

object Step {

  case class Field(field: String) extends Step
  case class Index(index: Int) extends Step

  implicit val byteSerializableStep: ByteSerializable[Step] = {
    case Field(field) => 0.toByte +: field.getBytes("UTF-8")
    case Index(index) => 1.toByte +: BigInteger.valueOf(index).toByteArray
  }
}
