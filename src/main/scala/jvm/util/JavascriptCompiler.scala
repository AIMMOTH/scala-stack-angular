package jvm.util

import com.github.scalafiddle.Optimizer

import java.util.logging.Logger
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import jvm.servletcontext.WebServletContextListener

object JavascriptCompiler {

  private lazy val log = Logger.getLogger(getClass.getName)

  def apply(request: HttpServletRequest, response: HttpServletResponse) = {
    
    val optimizer = request.getRequestURI match {
      case uri if uri.endsWith(".min.js") => Optimizer.Full
      case _ => Optimizer.Fast
    }
    
    response.getWriter.println(WebServletContextListener.script)
  }
}
