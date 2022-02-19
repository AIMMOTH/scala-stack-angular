package jvm.servletcontext

import com.github.aimmoth.scalajs.compiler.{ScalaJsCompiler, ScalaJsFile}

import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener
import javax.servlet.ServletContextEvent

import javax.servlet.ServletContext
import java.util.logging.Logger
import scala.io.Source
import com.github.scalafiddle.Optimizer

import java.io.InputStream

object WebServletContextListener {

  var script : String = null
}

@WebListener
class WebServletContextListener extends ServletContextListener {
  
  private lazy val log = Logger.getLogger(getClass.getName)

  // Ending slash is important!
  private lazy val scalaJsSource = "/scalajs-source/"

  lazy val sjsVersion = "sjs0.6"
  lazy val scalaVersion = "2.11"
  lazy val versions = sjsVersion + "_" + scalaVersion // "sjs0.6_2.11"
  
  lazy val relativeJarPath = "/WEB-INF/lib/"
  
  lazy val additionalLibs = List(
    ("scalajs-angulate", "0.2.4"),
    ("scalatags", "0.6.8"),
    ("scalajs-dom", "0.9.8"),
    ("sourcecode", "0.1.5"),
    ("scala-parser-combinators", "1.1.2"),
    ("upickle", "1.4.3"))
    .map(pair => pair._1 + s"_$versions-" + pair._2 + ".jar").toSet
    
  def contextDestroyed(context: ServletContextEvent): Unit = {
  }

  def contextInitialized(contextEvent: ServletContextEvent): Unit = {

    val context = contextEvent.getServletContext
    val loader: (String => InputStream) = (path) => contextEvent.getServletContext.getResourceAsStream(path)
    
    val scalaJsCompiler = ScalaJsCompiler()
    scalaJsCompiler.init(loader, relativeJarPath, additionalLibs)
    
    // Compile!
    import scala.collection.JavaConversions._

    // Mutable??
    def findSource(path: String): scala.collection.mutable.Set[String] =
      context.getResourcePaths(path).partition(_.endsWith("/")) match {
        case (folders, files) =>
          log.fine(folders.mkString(", "))
          files ++ (folders flatMap findSource)
      }

    def read(file: String) : ScalaJsFile = {
      log.fine(s"Adding $file to Scala JS compilation.")

      context.getResourceAsStream(file) match {
        case is => 
          val source = Source.fromInputStream(is, "UTF-8").mkString
          ScalaJsFile(file, source)
      }
    }

    findSource(scalaJsSource) map read match {
      case sources =>

        scalaJsCompiler.compileScalaJsStrings(sources.toList, Optimizer.Fast) match {
          case script if script.isRight =>
            WebServletContextListener.script = script.right.get
          case error =>
            log.severe(error.left.get)
        }
      }
  }
}