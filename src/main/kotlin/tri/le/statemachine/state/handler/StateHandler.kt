package tri.le.statemachine.state.handler

import org.apache.logging.log4j.ThreadContext
import tri.le.statemachine.base.Traceable
import tri.le.statemachine.state.States
import tri.le.statemachine.state.States.DUMMY
import tri.le.statemachine.uti.Log

abstract class StateHandler<D : Traceable> : Log {

  abstract val state: States

  protected abstract val checkToHandle: (data: D, executedCount: Int) -> Boolean
  protected abstract fun handle(data: D): States

  protected val notRetry: (data: D, executedCount: Int) -> Boolean = { _, executedCount -> executedCount == 0 }

  protected fun retryMaxAttempts(maxAttempts: Int): (D, Int) -> Boolean =
    { _: D, executedCount: Int -> executedCount <= maxAttempts }

  protected abstract val errorState: States

  protected abstract fun handleError(data: D, executedCount: Int)


  fun doHandle(data: D, executedCount: Int = 0): States {
    ThreadContext.clearAll()
    ThreadContext.put("executedCount", executedCount.toString())
    ThreadContext.put("traceId", data.traceId())

    if (!checkToHandle(data, executedCount)) {
      l.info { "Ignored execution" }
      return DUMMY
    }

    l.info("Start handling")
    return try {
      handle(data)
    } catch (e: Exception) {
      l.error("Error when handle. Change to state $errorState", e)
      handleError(data, executedCount)
      errorState
    } finally {
      l.info("End handling")
    }
  }
}
