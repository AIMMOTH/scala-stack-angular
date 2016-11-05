package scalajs.angular

import scala.scalajs.js.annotation.JSExport

import biz.enef.angulate.Controller
import biz.enef.angulate.Scope

@JSExport
class SimpleController($scope : Scope) extends Controller {
  
  @JSExport
  var number = 13
  
  @JSExport
  def increse() = number += 1
  
  @JSExport
  def decrese() = number -= 1
}