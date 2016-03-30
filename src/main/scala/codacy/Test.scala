package codacy

import java.util.logging.Logger

import play.api.libs.json.JsObject

import scala.util.{Success, Failure, Try}

class Test {

  type Person = (Int,String)

  def xmlFromDatabase():Try[JsObject] = ???

  def ageAndAddressFromJson(json:JsObject): Try[Person]

  def printPerson(person:Person): Unit = println(person)

  def bla() = {


    "string" match{
      case s if s.length < 6 => true
      case _ => false
    }
  }


  case class Foo(field:String, field2:Int)

  val myFoo = Foo("",8)

  val myOtherFoo = myFoo.copy(field = "fef",field2 = myFoo.field2 + 2)


  object E extends Enumeration{
    val one = Value(9,"fef")
    val two,three = Value
  }

  sealed trait Weekdays

  case object Monday extends Weekdays


  def main(): Try[Unit] = {

    for{
      json <- xmlFromDatabase()
      person <- ageAndAddressFromJson(json)
    } yield printPerson(person)

    /*result match{
      case Failure(err) =>
        Console.err.println(s"error occured ${err.getMessage}")
      case Success(_) => ()
    }*/



  }



}
