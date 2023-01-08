package tri.le.statemachine.state.handler

import org.springframework.stereotype.Component
import tri.le.statemachine.state.States
import tri.le.statemachine.state.States.*
import tri.le.statemachine.transfer.TransferInfo
import tri.le.statemachine.uti.Log

@Component
class ReserveHandler : StateHandler<TransferInfo>(), Log {

  override val state = RESERVE

  override val checkToHandle = notRetry

  override val errorState = RESERVE_FAILED

  override fun handleError(data: TransferInfo, executedCount: Int) {
    l.info("End transaction. Save transaction information into database with state")
  }

  override fun handle(data: TransferInfo): States {
    l.info("Reserve transaction")

    return TRANSFER
  }
}
