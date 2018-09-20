package scalajs.shared

import scalatags.Text.all._
import scalatags.stylesheet.StyleSheet
import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
class Stylisch extends StyleSheet {
  initStyleSheet()

  val hidden = cls(display := "none")
  
  override def customSheetName = Some("scalatags")
}

