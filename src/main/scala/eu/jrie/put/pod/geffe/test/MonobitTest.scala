package eu.jrie.put.pod.geffe.test

class MonobitTest(override protected val stream: LazyList[Boolean]) extends TestFIPS {

  private var n: Int = _

  override protected def runTest(): Unit = {
    n = stream.count(i => i)
    log(s"ones number is $n")
  }

  override protected def check(): Unit = {
    assert(n > 9725)
    assert(n < 10275)
  }
}
