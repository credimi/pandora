package com.credimi.pandora.input

import com.credimi.pandora.core.Tree

trait TreeFormat[T] {

  type Leaf

  def to(t: T): Tree[Unit, Leaf]
}
