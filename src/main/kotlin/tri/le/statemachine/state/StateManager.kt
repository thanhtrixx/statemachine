package tri.le.statemachine.state

import org.springframework.stereotype.Component
import tri.le.statemachine.state.States.INIT
import tri.le.statemachine.state.handler.DummyHandler
import tri.le.statemachine.state.handler.StateHandler
import tri.le.statemachine.transfer.TransferInfo
import tri.le.statemachine.uti.Log


@Component
class StateManager(
  handlers: List<StateHandler<TransferInfo>>,
  private val dummyHandler: DummyHandler
) : Log {

  private val handlerMap = handlers.associateBy { it.state }
  fun handle() {
    l.info("Handle")

    var handle = handlerMap[INIT]!!
    val transferInfo = TransferInfo("20230108-0001", "Alex", "Blob", 10.toBigInteger())

    while (true) {
      val nextState = handle.doHandle(transferInfo)
      l.info("State update from ${handle.state} to ${nextState}")
      if (!(nextState.isProcessing)) {
        break
      }

      handle = handlerMap.getOrDefault(nextState, dummyHandler)
    }
  }
}
