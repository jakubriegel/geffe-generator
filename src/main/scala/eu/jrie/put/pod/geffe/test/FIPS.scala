package eu.jrie.put.pod.geffe.test

object FIPS {
  def seriesTest(stream: LazyList[Boolean]): Boolean = new SeriesTest(stream).test()
  def longSeriesTest(stream: LazyList[Boolean]): Boolean = new LongSeriesTest(stream).test()
  def pokerTest(stream: LazyList[Boolean]): Boolean = new PokerTest(stream).test()
  def monobitTest(stream: LazyList[Boolean]): Boolean = new MonobitTest(stream).test()

  object withInfo {
    def seriesTest(stream: LazyList[Boolean]): (Boolean, String) = resultWithLog(new SeriesTest(stream))
    def longSeriesTest(stream: LazyList[Boolean]): (Boolean, String) = resultWithLog(new LongSeriesTest(stream))
    def pokerTest(stream: LazyList[Boolean]): (Boolean, String) = resultWithLog(new PokerTest(stream))
    def monobitTest(stream: LazyList[Boolean]): (Boolean, String) = resultWithLog(new MonobitTest(stream))

    private def resultWithLog(test: TestFIPS) = (test.test(), test.getLog)
  }
}
