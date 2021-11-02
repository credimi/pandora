package com.credimi.pandora.core

trait ByteSerializable[X] {

  def bytes(x: X): Array[Byte]
}

object ByteSerializable {

  implicit val byteSerializableArrayByte: ByteSerializable[Array[Byte]] =
    identity

  implicit class Bytes[X: ByteSerializable](x: X) {

    def bytes: Array[Byte] =
      implicitly[ByteSerializable[X]].bytes(x)
  }
}
