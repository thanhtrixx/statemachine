package tri.le.statemachine.state


enum class States(val isProcessing: Boolean = true) {
  INIT,
  INIT_FAILED(false),
  RESERVE,
  RESERVE_FAILED(false),
  TRANSFER,
  TRANSFER_FAILED(false),
  SUCCESS(false),

  // just default state
  DUMMY(false),
}

data class State(
  val name: String,
  val allowRetry: Boolean = false,
)

data class Flow(
  var name: String,
  val states: Set<State>,
  val numberHandler: Int = 8
)
