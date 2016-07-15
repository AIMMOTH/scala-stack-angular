package example

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import fiddle.ScalaJsCompiler
import javax.servlet.http.HttpServlet
import fiddle.Optimizer
import scala.io.Source

@WebServlet(name = "javascriptCompiler", urlPatterns = Array("/javascript.js"))
class ScalaCompiler extends HttpServlet {
  
  override def doGet(request : HttpServletRequest, response : HttpServletResponse) = {
    
    val is = request.getServletContext.getResourceAsStream("/scalajs/example/Main.scala")
    
    val source = Source.fromInputStream(is).mkString
    
    val compiler = new ScalaJsCompiler
    response.getWriter.println(compiler.compileScalaJsString(request.getServletContext, source, Optimizer.Fast,  "/WEB-INF/lib/"))
  }
}