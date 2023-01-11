package tri.le.statemachine

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import tri.le.statemachine.state.StateManager
import tri.le.statemachine.state.States
import tri.le.statemachine.state.handler.base.NextAction
import tri.le.statemachine.transfer.TransferInfo
import tri.le.statemachine.uti.Log

@SpringBootApplication
class StateMachineApplication(
  val stateManager: StateManager<TransferInfo>,
  val activeMq: ActiveMq
) : CommandLineRunner, Log {

  override fun run(vararg args: String) {
    l.info("Start complete")
    stateManager
      .handle(
        TransferInfo("230109-00001", "Alex", "Blob", 1000.toBigInteger()),
        NextAction(States.INIT, submitNewThread = true)
      )
  }
}


fun main(args: Array<String>) {
  runApplication<StateMachineApplication>(*args)
}
