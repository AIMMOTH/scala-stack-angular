package scalajs.shared

import scalatags.Text.all._
import org.slf4j.LoggerFactory
import jvm.builder.LoggerBuilder
import scalatags.Text._
import org.junit.Assert
import scala.xml.XML

class TestRoute {

  implicit def logger = LoggerBuilder(LoggerFactory.getLogger(getClass))
  
//  @Test
  def testRoute: Unit = {
//    Route(List("en-gb", "index.html") match {
//      case Some(Right(html)) =>
//        val rendered = html.render
//        val xml = XML.loadString(rendered)
//        xml.child match {
//          case Seq(head, body) =>
//            Assert.assertEquals(head.label, "head")
//            Assert.assertEquals(body.label, "body")
//            Assert.assertTrue(body.descendant.find(element => element.label == "a" && element \@ "href" == "https://github.com/AIMMOTH/scala-stack/tree/jquery").isDefined)
//        }
//      case Some(Left(error)) =>
//        throw new Exception(error)
//      case None =>
//        throw new Exception("Should have found page")
//    }

  }
  
}