package tri.le.statemachine.state

import org.apache.logging.log4j.ThreadContext
import org.springframework.stereotype.Component
import tri.le.statemachine.base.Traceable
import tri.le.statemachine.state.handler.base.NextAction
import tri.le.statemachine.state.handler.base.NoOpHandler
import tri.le.statemachine.state.handler.base.StateHandler
import tri.le.statemachine.uti.Log
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Component
class StateManager<D : Traceable>(
  handlers: List<StateHandler<D>>,
  private val noOpHandler: NoOpHandler<D>,
  private val stateTracker: StateTracker
) : Log {

  private val handlerMap = handlers.associateBy { it.state }

  private val threadPool = Executors.newFixedThreadPool(1028)
  private val scheduledThreadPool = Executors.newScheduledThreadPool(32)

  fun handle(data: D, nextAction: NextAction) {
    ThreadContext.put("traceId", data.traceId)

    if (!nextAction.state.isProcessing) {
      l.info{"Complete flow: $nextAction"}
      return
    }

    when {
      nextAction.delayMillis > 0 -> scheduledHandle(data, nextAction)

      nextAction.submitNewThread -> handleInNewThread(data, nextAction)

      else -> doHandle(data, nextAction)
    }
  }

  private fun handleInNewThread(data: D, nextAction: NextAction) {
    threadPool.submit { doHandle(data, nextAction) }
  }

  private fun scheduledHandle(data: D, nextAction: NextAction) {
    l.info { "Schedule next state ${nextAction.state}. Start processing in next ${nextAction.delayMillis}" }
    scheduledThreadPool.schedule(
      { doHandle(data, nextAction) },
      nextAction.delayMillis.toLong(), TimeUnit.MILLISECONDS
    )
  }

  private fun doHandle(data: D, nextAction: NextAction) {

    val state = nextAction.state

    val handler = handlerMap.getOrDefault(state, noOpHandler)
    val nextAction = handler.handle(data, nextAction.attemptedTimes)

    stateTracker.track(data.traceId, state, nextAction.state, nextAction.attemptedTimes)

    handle(data, nextAction)
  }
}
