package tri.le.statemachine.mock

import kotlinx.coroutines.delay
import org.springframework.stereotype.Component
import tri.le.statemachine.base.DomainException
import kotlin.random.Random

/**
 * @author TriLe
 * Service to mock actions of handlers. It randomly delays current thread and throw exception
 */
@Component
class MockService {

  /**
   *  Randomly delay current thread and throw exception
   */
  suspend fun doSomething(errorPercent: Int = 20) {
    delay(Random.nextLong(5000))

    if (Random.nextInt(100) + errorPercent > 100) {
      throw DomainException("Mocking exception. Error percent $errorPercent")
    }
  }
}
