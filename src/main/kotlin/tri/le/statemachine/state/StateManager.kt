package tri.le.statemachine.state

import org.springframework.stereotype.Component
import tri.le.statemachine.uti.Logger

@Component
class StateManager : Logger {

  fun handle() {
    l.info("Handle")
  }
}
