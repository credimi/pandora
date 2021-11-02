package com.credimi.pandora.core.hash

case class Hash(bytes: Vector[Byte])

object Hash {

  def apply(bytes: Byte*): Hash = Hash(Vector(bytes: _*))
}
