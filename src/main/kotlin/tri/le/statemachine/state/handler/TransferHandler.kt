package tri.le.statemachine.state.handler

import org.springframework.stereotype.Component
import tri.le.statemachine.mock.MockService
import tri.le.statemachine.state.States
import tri.le.statemachine.state.States.*
import tri.le.statemachine.state.handler.base.StateHandler
import tri.le.statemachine.transfer.TransferInfo
import tri.le.statemachine.uti.Log

@Component
class TransferHandler(
  private val mockService: MockService
) : StateHandler<TransferInfo>(), Log {

  override val state = TRANSFER

  override val isNeedToHandle = notRetry
  override val delayWhenRetry = fixedDelay(1000)

  override val errorState = TRANSFER_FAILED

  override fun doHandle(data: TransferInfo): States {

    mockService.doSomething(10)

    return TRANSACTION_HISTORY
  }
}
