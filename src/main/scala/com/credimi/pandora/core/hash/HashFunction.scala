package com.credimi.pandora.core.hash

import java.security.MessageDigest

trait HashFunction {

  def apply(bytes: Array[Byte]): Hash
}

object HashFunction {

  val sha256: HashFunction = { bytes =>
    Hash(MessageDigest.getInstance("SHA-256").digest(bytes).toVector)
  }
}
