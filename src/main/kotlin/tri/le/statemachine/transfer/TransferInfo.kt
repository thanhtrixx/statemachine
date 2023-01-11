package tri.le.statemachine.transfer

import tri.le.statemachine.base.Traceable
import java.math.BigInteger

data class TransferInfo(
  val transactionId: String,
  val from: String,
  val to: String,
  val amount: BigInteger
) : Traceable {

  override val traceId = transactionId
}
