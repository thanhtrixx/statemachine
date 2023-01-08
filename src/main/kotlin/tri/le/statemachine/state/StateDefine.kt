package tri.le.statemachine.state

data class State(
  val name: String,
  val allowRetry: Boolean = false,
)
data class Flow(
  var name: String,
  val states: Set<State>,
  val numberHandler: Int = 8
)
