package tri.le.statemachine.state.handler

import org.springframework.stereotype.Component
import tri.le.statemachine.state.States
import tri.le.statemachine.state.States.*
import tri.le.statemachine.state.handler.base.StateHandler
import tri.le.statemachine.transfer.TransferInfo
import tri.le.statemachine.uti.Log

@Component
class InitTransactionHandler : StateHandler<TransferInfo>(), Log {

  override val state = INIT

  override val isNeedToHandle = retryMaxAttempts(3)
  override val delayWhenRetry = multiplierDelay(1000)

  override val errorState = INIT_FAILED

  override fun doHandle(transferInfo: TransferInfo): States {
    l.info { "Init transaction $transferInfo" }
    l.info { "Save transaction" }

    return RESERVE
  }
}
