package com.credimi.pandora.core.path

case class Path(steps: Seq[Step]) {

  def :+(field: Step): Path = Path(steps :+ field)

  def +:(field: Step): Path = Path(field +: steps)

  lazy val lastStep: Option[Step] = steps.lastOption

  def parentPath: Option[Path] =
    lastStep.map(_ => Path(steps.dropRight(1)))
}

object Path {

  def empty: Path = Path(Seq.empty)

  // can't call it apply for erasure issues
  def make(steps: Step*): Path = Path(Seq(steps: _*))
}
