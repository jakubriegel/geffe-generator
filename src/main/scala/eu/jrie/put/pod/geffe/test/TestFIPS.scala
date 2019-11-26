package eu.jrie.put.pod.geffe.test

trait TestFIPS {
  protected val stream: LazyList[Boolean]

  private val infoBuilder = new StringBuilder()
  protected def log(msg: String): Unit = infoBuilder.append(msg)
  final def getLog: String = infoBuilder.toString()

  final def test(): Boolean = {
    runTest()
    try {
      check()
      true
    } catch {
      case _ : AssertionError => false
    }
  }

  protected def runTest(): Unit
  protected def check(): Unit
}
