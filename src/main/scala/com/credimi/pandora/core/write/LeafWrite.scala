package com.credimi.pandora.core.write

import com.credimi.pandora.core.path.Step
import com.credimi.pandora.core.write.LeafWrite.ALeaf

sealed trait LeafWrite[+L] {

  def toIndex: Option[Int] =
    this match {
      case ALeaf(Step.Index(index), _) => Option(index)
      case _                           => Option.empty
    }
}

object LeafWrite {

  case object NoLeaf extends LeafWrite[Nothing]
  case class ALeaf[+L](step: Step, leaf: L) extends LeafWrite[L]
}
