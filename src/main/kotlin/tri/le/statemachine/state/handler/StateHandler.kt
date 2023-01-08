package tri.le.statemachine.state.handler

import org.apache.logging.log4j.ThreadContext
import tri.le.statemachine.base.Traceable
import tri.le.statemachine.uti.Log

abstract class StateHandler<D : Traceable> : Log {

  abstract val name: String

  protected abstract val checkToHandle: (data: D, executedCount: Int) -> Boolean
  protected abstract fun handle(data: D)

  protected val notRetry: (data: D, executedCount: Int) -> Boolean = { _, executedCount -> executedCount == 0 }

  protected abstract val errorState: String

  protected abstract fun handleError(data: D, executedCount: Int)


  fun doHandle(data: D, executedCount: Int = 0) {
    ThreadContext.clearAll()
    ThreadContext.put("executedCount", executedCount.toString())
    ThreadContext.put("traceId", data.traceId())

    if (!checkToHandle(data, executedCount)) {
      l.info { "Ignored execution" }
      return
    }

    l.info("Start handling")
    try {
      handle(data)
    } catch (e: Exception) {
      l.error("Error when handle. Change to state $errorState", e)
      handleError(data, executedCount)
    }
    l.info("End handling")
  }

}
