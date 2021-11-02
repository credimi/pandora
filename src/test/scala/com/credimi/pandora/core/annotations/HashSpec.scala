package com.credimi.pandora.core.annotations

import com.credimi.pandora.core.DefaultHelpers.{ dict, hashFunction, int, list }
import com.credimi.pandora.core.annotations.HashSpec.{ hashAnnotatedTree, tree }
import com.credimi.pandora.core.hash.Hash
import com.credimi.pandora.core.{ annotations, Dict, Leaf, List }
import com.credimi.pandora.input.json.JsonLeaf
import org.specs2.mutable

object HashSpec {

  val tree = dict("x" -> list(dict("p" -> dict("f" -> dict("a" -> list(int(1), int(2)), "b" -> int(3))))))

  val hashAnnotatedTree: Dict[annotations.Hash[Unit], JsonLeaf.JInt] = Dict(
    annotations.Hash(Hash(-60, -109), ()),
    Map(
      "x" -> List(
        annotations.Hash(Hash(-104, -1), ()),
        Seq(
          Dict(
            annotations.Hash(Hash(108, 1), ()),
            Map(
              "p" -> Dict(
                annotations.Hash(Hash(-17, -59), ()),
                Map(
                  "f" -> Dict(
                    annotations.Hash(Hash(-37, -94), ()),
                    Map(
                      "a" -> List(
                        annotations.Hash(Hash(55, -30), ()),
                        Seq(
                          Leaf(
                            annotations.Hash(Hash(34, 21), ()),
                            JsonLeaf.JInt(1)
                          ),
                          Leaf(
                            annotations.Hash(Hash(-6, 97), ()),
                            JsonLeaf.JInt(2)
                          )
                        )
                      ),
                      "b" -> Leaf(
                        annotations.Hash(Hash(-112, 108), ()),
                        JsonLeaf.JInt(3)
                      )
                    )
                  )
                )
              )
            )
          )
        )
      )
    )
  )
}

class HashSpec extends mutable.Specification {

  "merkle(tree) === hashAnnotatedTree" >> {
    annotations.Hash.merkle(tree) must ===(hashAnnotatedTree)
  }
}
