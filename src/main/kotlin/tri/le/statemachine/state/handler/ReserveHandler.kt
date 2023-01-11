package tri.le.statemachine.state.handler

import org.springframework.stereotype.Component
import tri.le.statemachine.state.States
import tri.le.statemachine.state.States.*
import tri.le.statemachine.state.handler.base.StateHandler
import tri.le.statemachine.transfer.TransferInfo
import tri.le.statemachine.uti.Log

@Component
class ReserveHandler : StateHandler<TransferInfo>(), Log {

  override val state = RESERVE

  override val isNeedToHandle = notRetry
  override val delayWhenRetry = fixedDelay(0)

  override val errorState = RESERVE_FAILED

  override fun doHandle(data: TransferInfo): States {
    l.info("Reserve transaction")

    return TRANSFER
  }
}
