package com.credimi.pandora.core

import com.credimi.pandora.core
import com.credimi.pandora.core.hash.HashFunction.sha256
import com.credimi.pandora.core.hash.{ Hash, HashFunction }
import com.credimi.pandora.input.json.JsonLeaf

object DefaultHelpers {

  type A = Unit
  type L = JsonLeaf

  implicit val formats = org.json4s.DefaultFormats

  implicit val byteSerializableL: ByteSerializable[L] =
    jValue => org.json4s.native.Serialization.writePretty(jValue).getBytes("UTF-8")

  implicit val hashFunction: HashFunction = { bytes =>
    Hash(sha256(bytes).bytes.take(2))
  }

  def str(string: String) = core.Leaf[A, L]((), JsonLeaf.JString(string))
  def int(int: Int) = core.Leaf[A, L]((), JsonLeaf.JInt(int))
  def list(list: core.Tree[A, L]*) = core.List[A, L]((), list)
  def dict(dict: (String, core.Tree[A, L])*) = core.Dict[A, L]((), dict.toMap)
}
