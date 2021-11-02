package com.credimi.pandora.core.annotations

import com.credimi.pandora.core
import com.credimi.pandora.core.ByteSerializable.Bytes
import com.credimi.pandora.core._
import com.credimi.pandora.core.hash.HashFunction

case class Hash[A](hash: core.hash.Hash, annotation: A)

object Hash {

  def merkle[A, L: ByteSerializable](tree: Tree[A, L])(implicit hashFunction: HashFunction): Tree[Hash[A], L] =
    tree match {
      case Leaf(annotation, leaf) =>
        val hash = hashFunction(0.toByte +: leaf.bytes)
        Leaf(Hash(hash, annotation), leaf)
      case List(annotation, list) =>
        val merkleList: Seq[Tree[Hash[A], L]] = list.map(merkle[A, L])
        val hash = hashFunction(
          1.toByte +:
            merkleList
              .map(_.annotation.hash.bytes)
              .foldLeft(Array.empty[Byte])(_ ++ _)
        )
        List(Hash(hash, annotation), merkleList)
      case Dict(annotation, dict) =>
        val merkleDict = dict.view.mapValues(merkle[A, L]).toMap
        val hash = hashFunction(
          2.toByte +:
            merkleDict.map { case (k, v) => k.getBytes("UTF-8") ++ v.annotation.hash.bytes }
              .foldLeft(Array.empty[Byte])(_ ++ _)
        )
        Dict(Hash(hash, annotation), merkleDict)
    }
}
