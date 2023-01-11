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
  handlers: List<StateHandler<D>>, private val noOpHandler: NoOpHandler<D>,
) : Log {

  private val handlerMap = handlers.associateBy { it.state }

  private val threadPool = Executors.newFixedThreadPool(128)
  private val scheduledThreadPool = Executors.newScheduledThreadPool(16)

  fun handle(data: D, nextAction: NextAction) {
    l.info("Begin handling flow traceId: ${data.traceId}")

    val state = nextAction.state

    if (!state.isProcessing) {
      l.info("Complete flow: $nextAction")
      return
    }

    when {
      nextAction.delayMillis > 0 -> {
        l.info("Schedule next action. Start processing in next ${nextAction.delayMillis}")
        scheduledThreadPool.schedule(
          { doHandle(data, state) },
          nextAction.delayMillis.toLong(),
          TimeUnit.MICROSECONDS
        )
      }

      nextAction.submitNewThread -> threadPool.submit { doHandle(data, state) }

      else -> doHandle(data, state)
    }
  }

  private fun doHandle(data: D, state: States) {
    ThreadContext.put("traceId", data.traceId)

    val handler = handlerMap.getOrDefault(state, noOpHandler)
    val nextAction = handler.handle(data, 0)
    handle(data, nextAction)

  }
}
