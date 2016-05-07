package com.codacy.macros

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.reflect.macros.whitebox

class json(mode: String = jsonMacro.defaultMode) extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro jsonMacro.impl
}

private[macros] object jsonMacro {

  val defaultMode = "strict"

  def impl(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    def extractClassNameAndFieldsAndIsValue(classDecl: ClassDef): (Modifiers, TypeName, List[ValDef], Boolean) = {
      classDecl match {
        case t@q"${mods: Modifiers} class ${tpname: TypeName}[..$tparams] $ctorMods(..${paramss: List[ValDef]}) extends { ..$earlydefns } with ..$parents { $self => ..$stats }" =>
          val isValueClass = parents.exists {
            case tq"AnyVal" => true
            case _ => false
          }

          (mods, tpname, paramss, isValueClass)
        case _ =>
          c.abort(c.enclosingPosition, "annotation is not supported here")
      }
    }

    def modifiedCompanion(modifiers: Modifiers, compDeclOpt: Option[ModuleDef], format: ValDef, className: TypeName) = {
      compDeclOpt.map { case q"$mods object $obj extends ..$bases { ..$body }" =>
        q"""
          $mods object $obj extends ..$bases {
            ..$body
            $format
          }
        """
      }.getOrElse {
        // TODO: Find a better way to exclude the `CASE` flag
        import Flag._
        val mods = modifiers match {
          case _ if modifiers.hasFlag(PRIVATE) =>
            Modifiers(PRIVATE)
          case _ =>
            Modifiers()
        }
        // Create a fresh companion object with the formatter
        q"$mods object ${className.toTermName} { $format }"
      }
    }

    def jsonFormatter(className: TypeName, fields: List[ValDef], isValue: Boolean) = {
      val valName = TermName(c.freshName("Format"))

      fields.length match {
        case 0 =>
          c.abort(c.enclosingPosition, "cannot create json formatter for case class with no fields")
        case 1 if isValue =>
          q"""
            implicit lazy val $valName:play.api.libs.json.Format[$className]  = {

              import play.api.libs.json._
              Format(
                __.read[${fields.head.tpt}].map(v => new ${className.toTypeName}(v)),
                new Writes[$className] { def writes(o: $className) = Json.toJson(o.${fields.head.name}) }
              )
            }
          """
        case _ => q"""
          implicit lazy val $valName:play.api.libs.json.OFormat[$className] = {
            import play.api.libs.json._
            import com.codacy.macros.utils._

            Json.format[$className].asOFormat
          }
        """
      }
    }

    def modifiedDeclaration(classDecl: ClassDef, compDeclOpt: Option[ModuleDef] = None) = {
      val (modifiers, className, fields, isValue) = extractClassNameAndFieldsAndIsValue(classDecl)

      val asValue = c.prefix.tree match {
        case q"new $_(${mode: Tree})" =>
          util.Try(c.eval[String](c.Expr(mode))).map {
            case "strict" =>
              false
            //if the class has only one field write a value
            case "value" =>
              if (fields.length == 1)
                true
              else
                c.abort(c.enclosingPosition, s"""cannot use value mode on class with more than 1 parameter""")

            case unknown =>
              c.abort(c.enclosingPosition, s"""unknown mode: $unknown. valid modes are: "strict" or "value", for default behaviour don't pass a parameter""")
          }.getOrElse(
            c.abort(c.enclosingPosition, s"""parameter has to be of type String""")
          )
        //no parameter - default: if AnyVal write a value else write an object
        case _ => defaultMode == "value" && isValue
      }

      val format = jsonFormatter(className, fields, asValue)
      val compDecl = modifiedCompanion(modifiers, compDeclOpt, format, className)

      // Return both the class and companion object declarations
      c.Expr {
        q"""
          $classDecl
          $compDecl
        """
      }
    }

    annottees.map(_.tree) match {
      case (classDecl: ClassDef) :: Nil =>
        modifiedDeclaration(classDecl)
      case (classDecl: ClassDef) :: (compDecl: ModuleDef) :: Nil =>
        modifiedDeclaration(classDecl, Some(compDecl))
      case _ =>
        c.abort(c.enclosingPosition, "Invalid annottee")
    }
  }
}
