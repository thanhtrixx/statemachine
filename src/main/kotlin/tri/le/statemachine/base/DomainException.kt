package tri.le.statemachine.base


class DomainException(
  message: String,
  cause: Throwable? = null,
  private val extraInfo: Map<String, Any>? = null
) : Exception(message, cause) {

  companion object {
    private val EMPTY_STACK_TRACE = arrayOfNulls<StackTraceElement>(0)
  }

  override fun getStackTrace() = EMPTY_STACK_TRACE

  override fun fillInStackTrace() = this

  override fun toString() = StringBuilder()
    .append(message)
    .append(if (cause == null) "" else "\ncause: $cause")
    .append(if (this.extraInfo == null) "" else "\nextraInfo: $extraInfo")
    .toString()
}
