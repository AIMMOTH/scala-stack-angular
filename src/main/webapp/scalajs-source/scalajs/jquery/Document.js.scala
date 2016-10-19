package scalajs.jquery

import scala.scalajs.js.Any.fromBoolean
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.eval

import org.scalajs.jquery.jQuery

import scalajs.shared.Stylisch
import scalajs.shared.html.Id

@JSExport
class Document {
  
  @JSExport
  def ready() = {
    println("Compiled Scala JS started...")
    
    // Easy way to start foundation without Scala JS facade
    eval("$(document).foundation();")
    
    // Use this to get names of styling classes
    val styling = new Stylisch
    
    jQuery("#" + Id.javascriptAlert).addClass(s"${styling.hidden.name}")
    jQuery("#" + Id.resourcePostButton).prop("disabled", false)
    jQuery("#" + Id.resourceGetButton).prop("disabled", false)
  }
}