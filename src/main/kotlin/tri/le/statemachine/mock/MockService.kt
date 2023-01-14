package tri.le.statemachine.mock

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
  fun doSomething(errorPercent: Int = 20) {
    Thread.sleep(Random.nextLong(5000))

    if (Random.nextInt(100) + errorPercent > 100) {
      throw DomainException("Mocking exception")
    }
  }
}
