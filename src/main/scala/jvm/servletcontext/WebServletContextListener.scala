package jvm.servletcontext

import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener
import javax.servlet.ServletContextEvent
import com.github.aimmoth.scalajs.compiler.servlet.ScalaJsCompiler

object WebServletContextListener {
  var scalaJsCompiler : ScalaJsCompiler = null
}

@WebListener
class WebServletContextListener extends ServletContextListener {
  
//  lazy val scalaJsCompiler = new ScalaJsCompiler 
  
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

  def contextInitialized(context: ServletContextEvent): Unit = {
    val scalaJsCompiler = new ScalaJsCompiler
    scalaJsCompiler.init(context.getServletContext, relativeJarPath, additionalLibs)
    
    WebServletContextListener.scalaJsCompiler = scalaJsCompiler
  }
}