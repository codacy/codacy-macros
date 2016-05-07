package com.codacy.macros

import play.api.libs.json._

package object utils {

  implicit class WritesExtension[A](writes: Writes[A]) {
    def asOWrites: OWrites[A] = writes match {
      case wr: OWrites[A] => wr
      case _ => OWrites((a: A) => writes.writes(a).as[JsObject])
    }
  }

  implicit class FormatExtension[A](format: Format[A]) {
    def asOFormat: OFormat[A] = format match {
      case fmt: OFormat[A] => fmt
      case _ => OFormat(format, format.asOWrites)
    }
  }

}
