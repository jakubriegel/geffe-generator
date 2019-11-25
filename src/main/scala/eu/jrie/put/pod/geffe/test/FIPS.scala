package eu.jrie.put.pod.geffe.test

object FIPS {
  def seriesTest(stream: LazyList[Boolean]): Boolean = new SeriesTest(stream).test()
  def longSeriesTest(stream: LazyList[Boolean]): Boolean = new LongSeriesTest(stream).test()
  def pokerTest(stream: LazyList[Boolean]): Boolean = new PokerTest(stream).test()
  def monobitTest(stream: LazyList[Boolean]): Boolean = new MonobitTest(stream).test()
}
