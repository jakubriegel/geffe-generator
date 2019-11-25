package eu.jrie.put.pod.geffe.test

private class LongSeriesTest(override protected val stream: LazyList[Boolean]) extends TestFIPS {

  private var noLongSeries: Boolean = _

  override protected def runTest(): Unit = {
    var previous = stream.head
    var n = 0
    noLongSeries = true
    stream foreach { b => {
      if (b == previous) n += 1
      else {
        if (n >= 26) noLongSeries = false
        else n = 1
      }
      previous = b
    } }
  }

  override protected def check(): Unit = assert(noLongSeries)
}
