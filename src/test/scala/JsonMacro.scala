import com.codacy.macros._
import org.specs2.mutable.Specification
import play.api.libs.json.{JsObject, Json}

@json case class ObjectByDefault(param1: String, param2: Int)

@json case class ObjectByDefault1Param(param1: String)

@json("strict") case class ObjectByStrict(param1: String) extends AnyVal

@json case class ValueByDefault(param1: String) extends AnyVal

@json("value") case class ValueByParam(param1: String)

@json("strict") case class ObjectByUselessParam(param1: String, param2: Int)

@json case class JsonWrapper(param1: JsObject) extends AnyVal

@json("value") case class JsonWrapperByParam(param1: JsObject) extends AnyVal


class JsonSpecs extends Specification {

  "A json object" should {

    "be created" in {
      val myFoo = ObjectByDefault("param1", 2)

      Json.toJson(myFoo) mustEqual Json.toJson(myFoo)(Json.writes[ObjectByDefault])
    }

    "be created" in {
      val myFoo = ObjectByDefault1Param("param1")

      Json.toJson(myFoo) mustEqual Json.toJson(myFoo)(Json.writes[ObjectByDefault1Param])
    }

    "be created" in {
      val myFoo = ObjectByStrict("param1")

      Json.toJson(myFoo) mustEqual Json.toJson(myFoo)(Json.writes[ObjectByStrict])
    }

    "be created" in {
      val myFoo = ObjectByUselessParam("param1", 0)

      Json.toJson(myFoo) mustEqual Json.toJson(myFoo)(Json.writes[ObjectByUselessParam])
    }
  }

  "A json value" should {

    "be created" in {
      val myFoo = ValueByDefault("param1")

      Json.toJson(myFoo) mustEqual Json.toJson(myFoo)(Json.writes[ValueByDefault])
    }

    "be created" in {
      val myFoo = ValueByParam("param1")

      Json.toJson(myFoo) mustEqual Json.toJson(myFoo.param1)
    }

    "be created as an object" in {
      val myFoo = JsonWrapper(Json.obj("foo" -> "bar"))

      Json.toJson(myFoo) mustEqual Json.toJson(myFoo)(Json.writes[JsonWrapper])
    }

    "be created as an object" in {
      val myFoo = JsonWrapperByParam(Json.obj("foo" -> "bar"))

      Json.toJson(myFoo) mustEqual Json.toJson(myFoo.param1)
    }
  }

}