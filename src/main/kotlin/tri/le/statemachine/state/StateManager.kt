package tri.le.statemachine.state

import org.springframework.stereotype.Component
import tri.le.statemachine.state.handler.StateHandler
import tri.le.statemachine.transfer.TransferInfo
import tri.le.statemachine.uti.Log

@Component
class StateManager(
  handlers: List<StateHandler<TransferInfo>>
) : Log {

  private val handlerMap = handlers.associateBy { it.name }
  fun handle() {
    l.info("Handle")

    val handle = handlerMap["INIT_TRANSACTION"]
    val transferInfo = TransferInfo("20230108-0001", "Alex", "Blob", 10.toBigInteger())
    handle?.doHandle(transferInfo)
    handle?.doHandle(transferInfo, 1)
  }
}
