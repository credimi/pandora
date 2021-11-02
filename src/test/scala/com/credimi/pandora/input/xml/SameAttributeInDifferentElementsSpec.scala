package com.credimi.pandora.input.xml

import com.credimi.pandora.core.{ Dict, Leaf }
import com.credimi.pandora.input.json.JsonLeaf.JString
import com.credimi.pandora.input.xml.Xml.TreeFormatXml
import org.specs2.mutable.Specification

class SameAttributeInDifferentElementsSpec extends Specification {

  val elem =
    <A>
      <B X="First">alpha</B>
      <C X="Second">beta</C>
      <D X="Third"><E X="Fourth">gamma</E></D>
    </A>

  "Occurrencies of attribute X are always nested under the relevant element (DT-662)" >> {
    TreeFormatXml.to(elem) must ===(
      Dict(
        (),
        Map(
          "A" -> Dict(
            (),
            Map(
              "B" -> Dict((), Map("text" -> Leaf((), JString("alpha")), "X" -> Leaf((), JString("First")))),
              "C" -> Dict((), Map("text" -> Leaf((), JString("beta")), "X" -> Leaf((), JString("Second")))),
              "D" -> Dict(
                (),
                Map(
                  "X" -> Leaf((), JString("Third")),
                  "E" -> Dict((), Map("X" -> Leaf((), JString("Fourth")), "text" -> Leaf((), JString("gamma"))))
                )
              )
            )
          )
        )
      )
    )
  }
}
