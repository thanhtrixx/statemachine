package tri.le.statemachine.state.handler

import org.springframework.stereotype.Component
import tri.le.statemachine.transfer.TransferInfo
import tri.le.statemachine.uti.Log

@Component
class InitTransactionHandler : StateHandler<TransferInfo>(), Log {
  override fun handle(transferInfo: TransferInfo) {
    l.info { "Init transaction $transferInfo" }
  }

  override val checkToHandle = notRetry

  override val errorState = "INIT_FAILED"
  override fun handleError(data: TransferInfo, executedCount: Int) {
    l.info("Handling error")
  }
}
