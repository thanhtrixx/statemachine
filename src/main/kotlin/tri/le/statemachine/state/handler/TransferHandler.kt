package tri.le.statemachine.state.handler

import org.springframework.stereotype.Component
import tri.le.statemachine.state.States
import tri.le.statemachine.state.States.*
import tri.le.statemachine.transfer.TransferInfo
import tri.le.statemachine.uti.Log

@Component
class TransferHandler : StateHandler<TransferInfo>(), Log {

  override val state = TRANSFER

  override val checkToHandle = notRetry

  override val errorState = TRANSFER_FAILED

  override fun handleError(data: TransferInfo, executedCount: Int) {
    l.info("End transaction. Save transaction information into database with state")
  }

  override fun handle(data: TransferInfo): States {
    l.info("Transfer transaction")

    return SUCCESS
  }
}
