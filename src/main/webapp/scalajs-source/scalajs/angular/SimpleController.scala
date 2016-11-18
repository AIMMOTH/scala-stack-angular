package scalajs.angular

import scala.scalajs.js
import scala.scalajs.js.Any.fromFunction1
import scala.scalajs.js.annotation.JSExport

import biz.enef.angulate.Controller
import biz.enef.angulate.core.HttpService
import scalajs.shared.Resource
import scala.scalajs.js.JSON
import scalajs.angular.logic.FrontendLogic

@JSExport
class SimpleController($http : HttpService) extends Controller {

  var number = 13
  var id = "0"
  var output = ""
  var loaded = true

  def increase() = number = FrontendLogic.increase(number)

  def decrease() = number = FrontendLogic.decrease(number)

  def post() = $http.post("/api/v1/resource", Resource.toDynamic(number)).success{
      x : js.Object => 
        id = x.toString
      }

  def get() = $http.get("/api/v1/resource/" + id).success{
      x : js.Object => 
        output = JSON.stringify(x)
    }
}