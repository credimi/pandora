package com.credimi.pandora.core.annotations

import com.credimi.pandora.core.DefaultHelpers.hashFunction
import com.credimi.pandora.core.annotations.Ids.Ancestor
import com.credimi.pandora.core.annotations.IdsSpec.idsPathHashAnnotatedTree
import com.credimi.pandora.core.annotations.PathSpec.pathHashAnnotatedTree
import com.credimi.pandora.core.hash.Hash
import com.credimi.pandora.core.path.Path
import com.credimi.pandora.core.path.Step.{ Field, Index }
import com.credimi.pandora.core.{ annotations, Dict, Leaf, List }
import com.credimi.pandora.input.json.JsonLeaf
import org.specs2.mutable.Specification

object IdsSpec {

  val idsPathHashAnnotatedTree: Dict[annotations.Ids[annotations.Path[annotations.Hash[Unit]]], JsonLeaf.JInt] = Dict(
    annotations
      .Ids(
        id = Hash(0, 71),
        parentId = None,
        nonListAncestor = None,
        index = None,
        annotation = annotations.Path(Path(Seq()), annotations.Hash(Hash(-60, -109), ()))
      ),
    Map(
      "x" -> List(
        annotations.Ids(
          id = Hash(113, 120),
          parentId = Some(Hash(0, 71)),
          nonListAncestor = Some(Ancestor(Path(Seq()), Hash(0, 71))),
          index = None,
          annotation = annotations.Path(Path(Seq(Field("x"))), annotations.Hash(Hash(-104, -1), ()))
        ),
        Seq(
          Dict(
            annotations.Ids(
              id = Hash(-61, 5),
              parentId = Some(Hash(113, 120)),
              nonListAncestor = Some(Ancestor(Path(Seq()), Hash(0, 71))),
              index = Some(0),
              annotation = annotations.Path(Path(Seq(Field("x"), Index(0))), annotations.Hash(Hash(108, 1), ()))
            ),
            Map(
              "p" -> Dict(
                annotations.Ids(
                  id = Hash(108, 66),
                  parentId = Some(Hash(-61, 5)),
                  nonListAncestor = Some(Ancestor(Path(Seq(Field("x"), Index(0))), Hash(-61, 5))),
                  index = None,
                  annotation = annotations
                    .Path(Path(Seq(Field("x"), Index(0), Field("p"))), annotations.Hash(Hash(-17, -59), ()))
                ),
                Map(
                  "f" -> Dict(
                    annotations.Ids(
                      id = Hash(-99, 114),
                      parentId = Some(Hash(108, 66)),
                      nonListAncestor = Some(Ancestor(Path(Seq(Field("x"), Index(0), Field("p"))), Hash(108, 66))),
                      index = None,
                      annotation = annotations.Path(
                        Path(Seq(Field("x"), Index(0), Field("p"), Field("f"))),
                        annotations.Hash(Hash(-37, -94), ())
                      )
                    ),
                    Map(
                      "a" -> List(
                        annotations.Ids(
                          id = Hash(-75, -66),
                          parentId = Some(Hash(-99, 114)),
                          nonListAncestor =
                            Some(Ancestor(Path(Seq(Field("x"), Index(0), Field("p"), Field("f"))), Hash(-99, 114))),
                          index = None,
                          annotation = annotations.Path(
                            Path(Seq(Field("x"), Index(0), Field("p"), Field("f"), Field("a"))),
                            annotations.Hash(Hash(55, -30), ())
                          )
                        ),
                        Seq(
                          Leaf(
                            annotations.Ids(
                              id = Hash(-17, 24),
                              parentId = Some(Hash(-75, -66)),
                              nonListAncestor =
                                Some(Ancestor(Path(Seq(Field("x"), Index(0), Field("p"), Field("f"))), Hash(-99, 114))),
                              index = Some(0),
                              annotation = annotations.Path(
                                Path(Seq(Field("x"), Index(0), Field("p"), Field("f"), Field("a"), Index(0))),
                                annotations.Hash(Hash(34, 21), ())
                              )
                            ),
                            JsonLeaf.JInt(1)
                          ),
                          Leaf(
                            annotations.Ids(
                              id = Hash(-70, 32),
                              parentId = Some(Hash(-75, -66)),
                              nonListAncestor =
                                Some(Ancestor(Path(Seq(Field("x"), Index(0), Field("p"), Field("f"))), Hash(-99, 114))),
                              index = Some(1),
                              annotation = annotations.Path(
                                Path(Seq(Field("x"), Index(0), Field("p"), Field("f"), Field("a"), Index(1))),
                                annotations.Hash(Hash(-6, 97), ())
                              )
                            ),
                            JsonLeaf.JInt(2)
                          )
                        )
                      ),
                      "b" -> Leaf(
                        annotations.Ids(
                          id = Hash(-49, 110),
                          parentId = Some(Hash(-99, 114)),
                          nonListAncestor =
                            Some(Ancestor(Path(Seq(Field("x"), Index(0), Field("p"), Field("f"))), Hash(-99, 114))),
                          index = None,
                          annotation = annotations.Path(
                            Path(Seq(Field("x"), Index(0), Field("p"), Field("f"), Field("b"))),
                            annotations.Hash(Hash(-112, 108), ())
                          )
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

class IdsSpec extends Specification {

  s"ids(pathHashAnnotatedTree) === idsPathHashAnnotatedTree" >> {
    annotations.Ids.ids(pathHashAnnotatedTree) must ===(idsPathHashAnnotatedTree)
  }

}
