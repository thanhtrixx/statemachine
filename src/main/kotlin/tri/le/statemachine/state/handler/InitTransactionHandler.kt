package tri.le.statemachine.state.handler

import io.micrometer.common.lang.NonNull
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import tri.le.statemachine.state.StateManager
import tri.le.statemachine.transfer.TransferInfo
import tri.le.statemachine.uti.Log

@Component
class InitTransactionHandler(
  @NonNull @Lazy
  val stateManager: StateManager
) : StateHandler<TransferInfo>(), Log {

  override val name = "INIT_TRANSACTION"
  override fun handle(transferInfo: TransferInfo) {
    l.info { "Init transaction $transferInfo" }
  }

  override val checkToHandle = notRetry

  override val errorState = "INIT_FAILED"
  override fun handleError(data: TransferInfo, executedCount: Int) {
    l.info("Handling error")
  }
}
