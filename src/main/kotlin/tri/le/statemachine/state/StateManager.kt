package tri.le.statemachine.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import org.apache.logging.log4j.ThreadContext
import org.apache.logging.log4j.kotlin.CoroutineThreadContext
import org.springframework.stereotype.Component
import tri.le.statemachine.base.Traceable
import tri.le.statemachine.state.handler.base.NextAction
import tri.le.statemachine.state.handler.base.NoOpHandler
import tri.le.statemachine.state.handler.base.StateHandler
import tri.le.statemachine.uti.Log


@Component
class StateManager<D : Traceable>(
  handlers: List<StateHandler<D>>, private val noOpHandler: NoOpHandler<D>, private val stateTracker: StateTracker
) : Log {

  private val handlerMap = handlers.associateBy { it.state }

  private val coroutinePool = newFixedThreadPoolContext(256, "StateWorker")

  fun handle(data: D, action: NextAction) {
    ThreadContext.put("traceId", data.traceId)

    if (!action.state.isProcessing) {
      l.info { "Complete flow: $action" }
      return
    }

    doHandle(data, action)
  }


  private fun doHandle(data: D, action: NextAction) = CoroutineScope(coroutinePool).launch(CoroutineThreadContext()) {
    val state = action.state

    val delayMillis = action.delayMillis.toLong()
    if (delayMillis > 0) {
      l.info { "Schedule next state ${action.state}. Start processing in next $delayMillis" }
      delay(delayMillis)
    }

    val handler = handlerMap.getOrDefault(state, noOpHandler)
    val nextAction = handler.handle(data, action.attemptedTimes)

    stateTracker.track(data.traceId, state, nextAction.state, nextAction.attemptedTimes)

    handle(data, nextAction)
  }

}
