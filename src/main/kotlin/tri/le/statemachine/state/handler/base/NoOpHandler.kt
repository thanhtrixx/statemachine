package tri.le.statemachine.state.handler.base

import org.springframework.stereotype.Component
import tri.le.statemachine.base.Traceable
import tri.le.statemachine.state.States
import tri.le.statemachine.state.States.NONE
import tri.le.statemachine.uti.Log

@Component
class NoOpHandler<D : Traceable> : StateHandler<D>(), Log {

  override val state: States = NONE
  override val isNeedToHandle = notRetry
  override val delayWhenRetry = multiplierDelay(0)
  override val errorState = NONE

  override suspend fun doHandle(data: D) = NONE // no-op

}
