package tri.le.statemachine.state.handler.base

import org.apache.logging.log4j.ThreadContext
import tri.le.statemachine.base.Traceable
import tri.le.statemachine.state.States
import tri.le.statemachine.state.handler.base.NextAction.Companion.END_ACTION
import tri.le.statemachine.uti.Log

abstract class StateHandler<D : Traceable> : Log {

  abstract val state: States

  protected abstract val isNeedToHandle: (data: D, attemptedTimes: Int) -> Boolean

  protected abstract val delayWhenRetry: (attemptedTimes: Int) -> Int
  protected abstract suspend fun doHandle(data: D): States

  protected val notRetry: (data: D, attemptedTimes: Int) -> Boolean = { _, attemptedTimes -> attemptedTimes == 0 }

  protected fun retryMaxAttempts(maxAttempts: Int): (D, Int) -> Boolean =
    { _: D, attemptedTimes: Int -> attemptedTimes <= maxAttempts }

  protected fun fixedDelay(delay: Int): (Int) -> Int = { (delay) }

  protected fun multiplierDelay(multiplier: Int): (Int) -> Int =
    { attemptedTimes: Int -> (attemptedTimes * multiplier) }

  protected abstract val errorState: States

  suspend fun handle(data: D, attemptedTimes: Int = 0): NextAction {
    ThreadContext.put("traceId", data.traceId)
    ThreadContext.put("state", state.name)

    if (!isNeedToHandle(data, attemptedTimes)) {
      l.info("Ignored execution")
      return END_ACTION
    }

    l.info("Start handling")
    return try {
      doHandle(data)
        .toNextAction()
    } catch (e: Exception) {
      l.error("Error. Change to state $errorState", e)
      return handleError(data, attemptedTimes)
    } finally {
      l.info("End handling")
      ThreadContext.clearAll()
    }
  }

  protected open fun handleError(data: D, attemptedTimes: Int): NextAction {
    val nextAttemptedTimes = attemptedTimes + 1
    // check to retry
    if (isNeedToHandle(data, nextAttemptedTimes)) {
      l.info { "Retrying attemptedTimes = $nextAttemptedTimes" }
      return state
        .toNextAction(
          delayMillis = delayWhenRetry(nextAttemptedTimes),
          attemptedTimes = nextAttemptedTimes
        )
    }

    // or end handling
    return errorState
      .toNextAction()
  }

  private fun States.toNextAction(delayMillis: Int = 0, attemptedTimes: Int = 0, submitNewThread: Boolean = false) =
    NextAction(this, delayMillis, attemptedTimes, submitNewThread)
}
