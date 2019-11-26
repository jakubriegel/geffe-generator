package eu.jrie.put.pod.geffe.test

private class PokerTest(override protected val stream: LazyList[Boolean]) extends TestFIPS {

  private var poker: Double = _

  override protected def runTest(): Unit = {
    val s: Double = stream.sliding(4, 4).toList
      .groupBy(identity).view
      .mapValues(_.size).values
      .map(i => { log(s"$i\n"); i })
      .map(i => i*i)
      .map(i => i.toDouble)
      .sum

    poker = (16.0/5000.0 * s) - 5000.0
    log(s"poker value is $poker")
  }

  override protected def check(): Unit = {
    assert(poker > 2.16)
    assert(poker < 46.17)
  }
}
