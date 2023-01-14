package tri.le.statemachine.state.handler

import org.springframework.stereotype.Component
import tri.le.statemachine.mock.MockService
import tri.le.statemachine.state.States
import tri.le.statemachine.state.States.*
import tri.le.statemachine.state.handler.base.StateHandler
import tri.le.statemachine.transfer.TransferInfo
import tri.le.statemachine.uti.Log

@Component
class TransactionHistoryHandler(
  private val mockService: MockService
) : StateHandler<TransferInfo>(), Log {

  override val state = TRANSACTION_HISTORY

  override val isNeedToHandle = retryMaxAttempts(10)
  override val delayWhenRetry = multiplierDelay(1000)

  override val errorState = TRANSACTION_HISTORY_FAILED

  override suspend fun doHandle(transferInfo: TransferInfo): States {

    mockService.doSomething(80)

    return SUCCESS
  }
}
