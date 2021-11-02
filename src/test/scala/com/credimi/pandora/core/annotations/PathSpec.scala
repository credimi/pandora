package com.credimi.pandora.core.annotations

import com.credimi.pandora.core.annotations.HashSpec.hashAnnotatedTree
import com.credimi.pandora.core.annotations.PathSpec.pathHashAnnotatedTree
import com.credimi.pandora.core.hash.Hash
import com.credimi.pandora.core.path.Path
import com.credimi.pandora.core.path.Step.{ Field, Index }
import com.credimi.pandora.core.{ annotations, Dict, Leaf, List }
import com.credimi.pandora.input.json.JsonLeaf
import org.specs2.mutable

object PathSpec {

  val pathHashAnnotatedTree: Dict[annotations.Path[annotations.Hash[Unit]], JsonLeaf.JInt] = Dict(
    annotations.Path(Path(Seq()), annotations.Hash(Hash(-60, -109), ())),
    Map(
      "x" -> List(
        annotations.Path(Path(Seq(Field("x"))), annotations.Hash(Hash(-104, -1), ())),
        Seq(
          Dict(
            annotations.Path(Path(Seq(Field("x"), Index(0))), annotations.Hash(Hash(108, 1), ())),
            Map(
              "p" -> Dict(
                annotations
                  .Path(Path(Seq(Field("x"), Index(0), Field("p"))), annotations.Hash(Hash(-17, -59), ())),
                Map(
                  "f" -> Dict(
                    annotations.Path(
                      Path(Seq(Field("x"), Index(0), Field("p"), Field("f"))),
                      annotations.Hash(Hash(-37, -94), ())
                    ),
                    Map(
                      "a" -> List(
                        annotations.Path(
                          Path(Seq(Field("x"), Index(0), Field("p"), Field("f"), Field("a"))),
                          annotations.Hash(Hash(55, -30), ())
                        ),
                        Seq(
                          Leaf(
                            annotations.Path(
                              Path(Seq(Field("x"), Index(0), Field("p"), Field("f"), Field("a"), Index(0))),
                              annotations.Hash(Hash(34, 21), ())
                            ),
                            JsonLeaf.JInt(1)
                          ),
                          Leaf(
                            annotations.Path(
                              Path(Seq(Field("x"), Index(0), Field("p"), Field("f"), Field("a"), Index(1))),
                              annotations.Hash(Hash(-6, 97), ())
                            ),
                            JsonLeaf.JInt(2)
                          )
                        )
                      ),
                      "b" -> Leaf(
                        annotations.Path(
                          Path(Seq(Field("x"), Index(0), Field("p"), Field("f"), Field("b"))),
                          annotations.Hash(Hash(-112, 108), ())
                        ),
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

class PathSpec extends mutable.Specification {

  s"paths(hashAnnotatedTree) === pathHashAnnotatedTree" >> {
    annotations.Path.paths(hashAnnotatedTree) === pathHashAnnotatedTree
  }
}
