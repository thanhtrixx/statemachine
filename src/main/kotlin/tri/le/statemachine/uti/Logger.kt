package tri.le.statemachine.uti

import org.apache.logging.log4j.kotlin.cachedLoggerOf

interface Logger {
  val l
    get() = cachedLoggerOf(this.javaClass)
}
