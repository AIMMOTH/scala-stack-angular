package jvm.builder

import scalajs.shared.util.JsLogger
import java.util.logging.Logger
import java.util.logging.Level

object LoggerBuilder {
  
  def apply(log : Logger) = {
    new JsLogger {
      def info(message : String) = log.info(message)
      def debug(message : String) = log.fine(message)
      def warning(message : String) = log.warning(message)
      def warning(throwable : Throwable) = log.log(Level.WARNING, throwable.getMessage, throwable)
      def error(message : String) = log.warning(message)
      def error(throwable : Throwable) = log.log(Level.WARNING, throwable.getMessage, throwable)
    }
  }
}