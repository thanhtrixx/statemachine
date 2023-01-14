package tri.le.statemachine.state

import org.springframework.stereotype.Component
import java.time.Instant

/**
 * @author TriLe
 */
@Component
class StateTracker {

  // For example, just use list. In real world should be stored in the database
  private val historical = mutableListOf<StateLog>()

  fun track(traceId: String, fromState: States, toState: States, attemptedTimes: Int) {
    historical.add((StateLog(traceId, fromState, toState, attemptedTimes)))
  }
}

data class StateLog(
  val traceId: String,
  val fromState: States,
  val toState: States,
  val attemptedTimes: Int,
  val current: Instant = Instant.ofEpochMilli(System.currentTimeMillis())
)
