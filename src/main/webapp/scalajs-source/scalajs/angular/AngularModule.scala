package scalajs.angular

import scala.scalajs.js.annotation.JSExport

import biz.enef.angulate.Angular.RichAngular
import biz.enef.angulate.angular

@JSExport
class AngularModule {
  
  @JSExport
  def init() = {
    angular.createModule("app", Seq()) match {
      case app => 
        app.controllerOf[SimpleController]("scalajs.angular.SimpleController")
    }  
  }
}