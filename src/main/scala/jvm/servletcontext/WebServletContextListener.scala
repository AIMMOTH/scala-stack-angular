package jvm.servletcontext

import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener
import javax.servlet.ServletContextEvent
import com.github.aimmoth.scalajs.compiler.servlet.ScalaJsCompiler
import javax.servlet.ServletContext
import java.util.logging.Logger
import scala.io.Source
import com.github.aimmoth.scalajs.compiler.servlet.Optimizer

object WebServletContextListener {

//  var scalaJsCompiler : ScalaJsCompiler = null
  var script : String = null
  
}

@WebListener
class WebServletContextListener extends ServletContextListener {
  
  private lazy val log = Logger.getLogger(getClass.getName)

//  lazy val scalaJsCompiler = new ScalaJsCompiler 
  
  // Ending slash is important!
  private lazy val scalaJsSource = "/scalajs-source/"

  lazy val sjsVersion = "sjs0.6"
  lazy val scalaVersion = "2.11"
  lazy val versions = sjsVersion + "_" + scalaVersion // "sjs0.6_2.11"
  
  lazy val relativeJarPath = "/WEB-INF/lib/"

  /*
   * Important! These must be compiled to Scala JS!
   */
  lazy val additionalLibs = Set(
    s"scalajs-angulate_$versions-0.2.4.jar",
    s"scalatags_$versions-0.6.0.jar",
    s"scalajs-dom_$versions-0.9.1.jar",
    s"sourcecode_$versions-0.1.1.jar",
    s"scala-parser-combinators_$versions-1.0.2.jar",
    s"upickle_$versions-0.4.1.jar"
    )
  
  def contextDestroyed(context: ServletContextEvent): Unit = {
  }

  def contextInitialized(contextEvent: ServletContextEvent): Unit = {
    
    val context = contextEvent.getServletContext
    
    val scalaJsCompiler = new ScalaJsCompiler
    scalaJsCompiler.init(context, relativeJarPath, additionalLibs)
    
    // Compile!
    import scala.collection.JavaConversions._

    // Mutable??
    def findSource(path: String): scala.collection.mutable.Set[String] =
      context.getResourcePaths(path).partition(_.endsWith("/")) match {
        case (folders, files) =>
          log.fine(folders.mkString(", "))
          files ++ (folders flatMap findSource)
      }

    def read(file: String) = {
      log.fine(s"Adding $file to Scala JS compilation.")

      context.getResourceAsStream(file) match {
        case is => 
          Source.fromInputStream(is, "UTF-8").mkString
      }
    }

    findSource(scalaJsSource) map read match {
      case sources =>

        scalaJsCompiler.compileScalaJsStrings(sources.toList, Optimizer.Fast) match {
          case script: String => 
            WebServletContextListener.script = script
        }
      }
  }
}