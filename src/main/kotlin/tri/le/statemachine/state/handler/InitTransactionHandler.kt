package tri.le.statemachine.state.handler

import org.springframework.stereotype.Component
import tri.le.statemachine.state.States
import tri.le.statemachine.state.States.*
import tri.le.statemachine.transfer.TransferInfo
import tri.le.statemachine.uti.Log

@Component
class InitTransactionHandler : StateHandler<TransferInfo>(), Log {

  override val state = INIT

  override val checkToHandle = retryMaxAttempts(3)

  override val errorState = INIT_FAILED

  override fun handle(transferInfo: TransferInfo): States {
    l.info { "Init transaction $transferInfo" }
    l.info { "Save transaction" }

    return RESERVE
  }

  override fun handleError(data: TransferInfo, executedCount: Int) {
    l.info("Handling error")
  }
}
