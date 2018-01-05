package jvm.util

import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import scala.io.Source
import com.github.aimmoth.scalajs.compiler.servlet.Optimizer
import com.github.aimmoth.scalajs.compiler.servlet.ScalaJsCompiler
import java.util.logging.Logger
import jvm.servletcontext.WebServletContextListener

object JavascriptCompiler {

  private lazy val log = Logger.getLogger(getClass.getName)

  // Ending slash is important!
  private lazy val scalaJsSource = "/scalajs-source/"


  def apply(request: HttpServletRequest, response: HttpServletResponse) = {

    import scala.collection.JavaConversions._

    // Mutable??
    def findSource(path: String): scala.collection.mutable.Set[String] =
      request.getServletContext.getResourcePaths(path).partition(_.endsWith("/")) match {
        case (folders, files) =>
          log.fine(folders.mkString(", "))
          files ++ (folders flatMap findSource)
      }

    def read(file: String) = {
      log.fine(s"Adding $file to Scala JS compilation.")
      println(s"Adding $file to Scala JS compilation.")

      request.getServletContext.getResourceAsStream(file) match {
        case is => 
          Source.fromInputStream(is, "UTF-8").mkString
      }
    }

    findSource(scalaJsSource) map read match {
      case sources =>
        val optimizer = request.getRequestURI match {
          case uri if uri.endsWith(".min.js") => Optimizer.Full
          case _ => Optimizer.Fast
        }

        WebServletContextListener.scalaJsCompiler.compileScalaJsStrings(sources.toList, optimizer) match {
          case script: String => response.getWriter.println(script)
        }
      }

  }
}
