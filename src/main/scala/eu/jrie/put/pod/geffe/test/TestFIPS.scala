package eu.jrie.put.pod.geffe.test

trait TestFIPS {
  protected val stream: LazyList[Boolean]

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
