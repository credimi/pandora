package com.credimi.pandora.core.rewriting

import com.credimi.pandora.core.DefaultHelpers.{ dict, int, list }
import org.json4s.native.Serialization._
import org.specs2.mutable.Specification

object RewritingSpec {

  val t1 = list(dict("a" -> dict("b" -> int(10)), "c" -> int(8)))
  val t1Flattened = list(dict("a_b" -> int(10), "c" -> int(8)))
  val t2 = dict("p" -> dict("f" -> dict("a" -> int(1), "b" -> int(2), "c" -> int(3))))
  val t2Flattened = dict("p_f_a" -> int(1), "p_f_b" -> int(2), "p_f_c" -> int(3))
  val t3 = dict("p" -> dict("f" -> dict("a" -> list(int(1), int(2)), "b" -> int(3))))
  val t3Flattened = dict("p_f_a" -> list(int(1), int(2)), "p_f_b" -> int(3))
  val t4 = list(t3)
  val t4Flattened = list(t3Flattened)
  val t5 = dict("x" -> t4)
  val t5Flattened = dict("x" -> t4Flattened)
  val t6 = dict("y" -> t5)
  val t6Flattened = dict("y_x" -> t4Flattened)
}

class RewritingSpec extends Specification {

  import com.credimi.pandora.core.DefaultHelpers._
  import RewritingSpec._

  for (
    (t, tFlattened) <- Seq(
      (t1, t1Flattened),
      (t2, t2Flattened),
      (t3, t3Flattened),
      (t4, t4Flattened),
      (t5, t5Flattened),
      (t6, t6Flattened)
    )
  )
    s"flattenNestedDicts(${write(t)}) === ${write(tFlattened)}" >> {
      flattenNestedDicts("_")(t) must ===(tFlattened)
    }

}
