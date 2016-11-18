package trans

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import com.google.appengine.tools.development.testing.LocalServiceTestHelper
import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity

import jvm.api.logic.BackendLogic
import jvm.datastore.Objectify
import com.googlecode.objectify.ObjectifyService
import com.googlecode.objectify.util.Closeable
import org.slf4j.LoggerFactory
import com.google.gson.Gson
import scalajs.shared.util.OK
import scalajs.shared.util.KO
import jvm.builder.LoggerBuilder
import scalajs.angular.logic.FrontendLogic

class TransTest {

  lazy val gson = new Gson
  val logger = LoggerFactory.getLogger(getClass)
  implicit def jsLogger = LoggerBuilder(logger)
  val helper = new LocalServiceTestHelper()
  var closable : Closeable = null

  @Before
  def before : Unit = {
    helper.setUp()
    Objectify.registerClasses()
    closable = ObjectifyService.begin()
  }

  @After
  def after : Unit = {
    closable.close()
    helper.tearDown()
  }

  @Test
  def post : Unit = {
    Assert.assertTrue(FrontendLogic.increase(1) == 2)
    Assert.assertTrue(FrontendLogic.decrease(1) == 0)
  }
}