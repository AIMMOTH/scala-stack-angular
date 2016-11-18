package scalajs.shared.html

import scalatags.Text.all._
import scalatags.stylesheet._
import scalajs.shared.Languages
import scalajs.shared.Stylisch
import scalajs.shared.Translations
import scalatags.Text.all.{ `type` => type_ }
import scalatags.Text.tags2.{ style => style2, title => title2 }
import biz.enef.angulate.ScopeController
import scalajs.angular.SimpleController
import scalajs.angular.SimpleController

object Id extends Enumeration {
  val javascriptAlert, resourcePostButton, resourceGetButton, resourcePost, resourceGet, resourceOutput = Value
}

object Index {

  def apply(css : Stylisch, minified : Boolean = false, language : Languages.Language = Languages.default) = {

    // From reflection get "package.Class().method()"
    def methodPath(klass : Class[_], methodName : String) = s"${klass.getCanonicalName}().$methodName()"

    /*
     * Get paths to classes with code to prevent refactoring mistakes.
     */
    val simpleController = classOf[scalajs.angular.SimpleController].getCanonicalName // scalajs.angular.SimpleController
    val documentReady = methodPath(classOf[scalajs.angular.AngularModule], "init") // scalajs.angular.AngularModule().init()

    val min = if (minified) ".min" else ""

    html(
      head(
        title2("All Scala!"),
        /*
         * TODO: All css files could be read from resources and bundled into one.
         */
        link(rel := "stylesheet", href := s"/css/foundation$min.css"), // http://foundation.zurb.com/sites/docs/kitchen-sink.html
        link(rel := "stylesheet", href := s"/css/foundation-icons.css"), // http://zurb.com/playground/foundation-icon-fonts-3
        style2(type_ := "text/css")(css.styleSheetText)),

      body(attr("ng-app") := "app")(
        div(cls := "row")(
          div(cls := "large-12 columns")(

            div(attr("ng-controller") := simpleController + " as controller")(

              div(cls := "callout alert", attr("ng-hide") := "controller.loaded")(
                h5("Compiling Scala JS to JavaScript ..."),
                p("Backend is now compiling Scala JS source code into a JavaScript, it should take a few seconds. Buttons are disabled in the meanwhile.")),

              h1(Translations.documentHeader.get(language)),

              h2("POST Resource"),
              p("Enter number and ..."),
              input(type_ := "number", attr("ng-model") := "controller.number", attr("ng-disabled") := "!controller.loaded", disabled := true),
              //              p("Number is: {{ controller.number }}"),
              button(attr("ng-click") := "controller.increase()", cls := "button", attr("ng-disabled") := "!controller.loaded", disabled := true)("Increase"),
              button(attr("ng-click") := "controller.decrease()", cls := "button", attr("ng-disabled") := "!controller.loaded", disabled := true)("Decrease"),
              p("... create a resource with POST"),
              button(attr("ng-click") := "controller.post()", cls := "button", attr("ng-disabled") := "!controller.loaded", disabled := true)("Post"),

              h2("GET Resource"),
              p("GET a resource with a id provided from POST."),
              input(type_ := "text", attr("ng-model") := "controller.id", attr("ng-disabled") := "!controller.loaded", disabled := true),
              button(attr("ng-click") := "controller.get()", cls := "button", attr("ng-disabled") := "!controller.loaded", disabled := true)("Get"),
              textarea(disabled := true, attr("ng-model") := "controller.output")),

            p("Source at ")(
              a(target := "_blank", href := "https://github.com/AIMMOTH/scala-stack-angular")("GitHub")),

            /*
       * TODO: All javascript could be read from resources and bundled into one file.
       */
            script(src := "/js/vendor/angular-1.5.8.js"),
            script(src := "/js/vendor/jquery.js"),
            script(src := "/js/vendor/what-input.js"),
            script(src := s"/js/vendor/foundation$min.js"),
            script(src := s"/javascript-${System.currentTimeMillis()}$min.js"),
            /*
       * Start Foundation
       */
            script()(documentReady)))))
  }
}