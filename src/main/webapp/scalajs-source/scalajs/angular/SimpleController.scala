package scalajs.angular

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

import biz.enef.angulate.Controller
import biz.enef.angulate.core.HttpService
import scalajs.shared.Resource
import scalajs.shared.ResourceBuilder

@JSExport
class SimpleController($http : HttpService) extends Controller {

  var number = 13
  var id = "0"

  def increse() = number += 1

  def decrese() = number -= 1

  def post() = {
    $http.post("/api/v1/resource", ResourceBuilder.toDynamic(number)).success{
      x : js.Object => 
        println(s"hej $x")}
  }

  def get() = {
    $http.get("/api/v1/resource/" + id).success{
      x : js.Object => 
        println(s"hej $x")}
  }
}