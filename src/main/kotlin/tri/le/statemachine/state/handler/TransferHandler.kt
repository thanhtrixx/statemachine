package tri.le.statemachine.state.handler

import org.springframework.stereotype.Component
import tri.le.statemachine.state.States
import tri.le.statemachine.state.States.*
import tri.le.statemachine.state.handler.base.NextAction
import tri.le.statemachine.state.handler.base.StateHandler
import tri.le.statemachine.transfer.TransferInfo
import tri.le.statemachine.uti.Log

@Component
class TransferHandler : StateHandler<TransferInfo>(), Log {

  override val state = TRANSFER

  override val isNeedToHandle = notRetry
  override val delayWhenRetry = fixedDelay(1000)

  override val errorState = TRANSFER_FAILED

  override fun handleError(data: TransferInfo, attemptedTimes: Int): NextAction {
    l.info("End transaction. Save transaction information into database with state")
    return TRANSFER_FAILED.toNextAction()
  }

  override fun doHandle(data: TransferInfo): States {
    l.info("Transfer transaction")

    return SUCCESS
  }
}
