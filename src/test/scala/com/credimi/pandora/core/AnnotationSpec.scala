package com.credimi.pandora.core

import com.credimi.pandora.core
import com.credimi.pandora.core.DefaultHelpers.{ dict, int, list, str }
import org.json4s.native.Serialization._
import org.specs2.mutable

object AnnotationSpec {

  val tree0 = dict("x" -> int(1), "y" -> str("test"))
  val tree1 = list(int(4), int(2), int(4))
}

class AnnotationSpec extends mutable.Specification {

  import DefaultHelpers._
  import AnnotationSpec._

  s"""${write(tree0)} is annotated correctly""" >> {
    writePretty(core.annotated((tree0))) must ===(
      """{
        |  "annotation":{
        |    "id":{
        |      "bytes":[
        |        -90,
        |        -66
        |      ]
        |    },
        |    "annotation":{
        |      "path":{
        |        "steps":[
        |          
        |        ]
        |      },
        |      "annotation":{
        |        "hash":{
        |          "bytes":[
        |            109,
        |            -116
        |          ]
        |        },
        |        "annotation":{
        |          
        |        }
        |      }
        |    }
        |  },
        |  "dict":{
        |    "x":{
        |      "annotation":{
        |        "id":{
        |          "bytes":[
        |            -56,
        |            -90
        |          ]
        |        },
        |        "parentId":{
        |          "bytes":[
        |            -90,
        |            -66
        |          ]
        |        },
        |        "nonListAncestor":{
        |          "path":{
        |            "steps":[
        |              
        |            ]
        |          },
        |          "id":{
        |            "bytes":[
        |              -90,
        |              -66
        |            ]
        |          }
        |        },
        |        "annotation":{
        |          "path":{
        |            "steps":[
        |              {
        |                "field":"x"
        |              }
        |            ]
        |          },
        |          "annotation":{
        |            "hash":{
        |              "bytes":[
        |                -10,
        |                120
        |              ]
        |            },
        |            "annotation":{
        |              
        |            }
        |          }
        |        }
        |      },
        |      "leaf":{
        |        "value":1
        |      }
        |    },
        |    "y":{
        |      "annotation":{
        |        "id":{
        |          "bytes":[
        |            -51,
        |            -37
        |          ]
        |        },
        |        "parentId":{
        |          "bytes":[
        |            -90,
        |            -66
        |          ]
        |        },
        |        "nonListAncestor":{
        |          "path":{
        |            "steps":[
        |              
        |            ]
        |          },
        |          "id":{
        |            "bytes":[
        |              -90,
        |              -66
        |            ]
        |          }
        |        },
        |        "annotation":{
        |          "path":{
        |            "steps":[
        |              {
        |                "field":"y"
        |              }
        |            ]
        |          },
        |          "annotation":{
        |            "hash":{
        |              "bytes":[
        |                -40,
        |                15
        |              ]
        |            },
        |            "annotation":{
        |              
        |            }
        |          }
        |        }
        |      },
        |      "leaf":{
        |        "value":"test"
        |      }
        |    }
        |  }
        |}""".stripMargin
    )
  }

  s"""${write(tree1)} is annotated correcly""" >> {
    writePretty(core.annotated((tree1))) must ===(
      """{
        |  "annotation":{
        |    "id":{
        |      "bytes":[
        |        -63,
        |        -60
        |      ]
        |    },
        |    "annotation":{
        |      "path":{
        |        "steps":[
        |          
        |        ]
        |      },
        |      "annotation":{
        |        "hash":{
        |          "bytes":[
        |            -99,
        |            87
        |          ]
        |        },
        |        "annotation":{
        |          
        |        }
        |      }
        |    }
        |  },
        |  "list":[
        |    {
        |      "annotation":{
        |        "id":{
        |          "bytes":[
        |            107,
        |            -51
        |          ]
        |        },
        |        "parentId":{
        |          "bytes":[
        |            -63,
        |            -60
        |          ]
        |        },
        |        "index":0,
        |        "annotation":{
        |          "path":{
        |            "steps":[
        |              {
        |                "index":0
        |              }
        |            ]
        |          },
        |          "annotation":{
        |            "hash":{
        |              "bytes":[
        |                -76,
        |                -39
        |              ]
        |            },
        |            "annotation":{
        |              
        |            }
        |          }
        |        }
        |      },
        |      "leaf":{
        |        "value":4
        |      }
        |    },
        |    {
        |      "annotation":{
        |        "id":{
        |          "bytes":[
        |            -31,
        |            -19
        |          ]
        |        },
        |        "parentId":{
        |          "bytes":[
        |            -63,
        |            -60
        |          ]
        |        },
        |        "index":1,
        |        "annotation":{
        |          "path":{
        |            "steps":[
        |              {
        |                "index":1
        |              }
        |            ]
        |          },
        |          "annotation":{
        |            "hash":{
        |              "bytes":[
        |                111,
        |                -65
        |              ]
        |            },
        |            "annotation":{
        |              
        |            }
        |          }
        |        }
        |      },
        |      "leaf":{
        |        "value":2
        |      }
        |    },
        |    {
        |      "annotation":{
        |        "id":{
        |          "bytes":[
        |            -57,
        |            -39
        |          ]
        |        },
        |        "parentId":{
        |          "bytes":[
        |            -63,
        |            -60
        |          ]
        |        },
        |        "index":2,
        |        "annotation":{
        |          "path":{
        |            "steps":[
        |              {
        |                "index":2
        |              }
        |            ]
        |          },
        |          "annotation":{
        |            "hash":{
        |              "bytes":[
        |                -76,
        |                -39
        |              ]
        |            },
        |            "annotation":{
        |              
        |            }
        |          }
        |        }
        |      },
        |      "leaf":{
        |        "value":4
        |      }
        |    }
        |  ]
        |}""".stripMargin
    )
  }

}
