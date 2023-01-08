package tri.le.statemachine

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import tri.le.statemachine.state.StateManager
import tri.le.statemachine.uti.Log

@SpringBootApplication
class StateMachineApplication(
  val stateManager: StateManager,
  val activeMq: ActiveMq
) : CommandLineRunner, Log {

  override fun run(vararg args: String) {
    l.info("Start complete")
    stateManager.handle()
  }
}


fun main(args: Array<String>) {
  runApplication<StateMachineApplication>(*args)
}
