package tri.le.statemachine.state.handler.base

import tri.le.statemachine.state.States

data class NextAction(
  val state: States,
  val delayMillis: Int = 0,
  val attemptedTimes: Int = 0,
  val submitNewThread: Boolean = false
) {
  companion object {
    val END_ACTION = NextAction(States.NONE)
  }
}
