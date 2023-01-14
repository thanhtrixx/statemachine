package tri.le.statemachine

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import tri.le.statemachine.state.StateManager
import tri.le.statemachine.state.States
import tri.le.statemachine.state.handler.base.NextAction
import tri.le.statemachine.transfer.TransferInfo
import tri.le.statemachine.uti.Log
import kotlin.random.Random

@SpringBootApplication
class StateMachineApplication(
  val stateManager: StateManager<TransferInfo>
) : CommandLineRunner, Log {

  override fun run(vararg args: String) {
    l.info("Start complete")



    for (i in 1..10) {
      stateManager
        .handle(
          TransferInfo(i.toString().padStart(10, '0'), randomName(), randomName(), Random.nextInt(1000).toBigInteger()),
          NextAction(States.INIT, submitNewThread = true)
        )
    }

  }

  private val userList = listOf(
    "Alex",
    "Blob",
    "Peter",
    "John",
    "Tim",
    "Adam",
    "Andrew",
    "Chris",
  )

  private fun randomName() = userList[Random.nextInt(userList.size)]
}


fun main(args: Array<String>) {
  runApplication<StateMachineApplication>(*args)
}
