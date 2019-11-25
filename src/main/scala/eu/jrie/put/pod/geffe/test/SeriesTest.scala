package eu.jrie.put.pod.geffe.test

import scala.annotation.tailrec
import scala.collection.immutable.ListMap
import scala.collection.mutable

private class SeriesTest(override protected val stream: LazyList[Boolean]) extends TestFIPS {

  private var series: Map[Int, Int] = _
  def result: Map[Int, Int] = ListMap.newBuilder.addAll(series.toSeq.sorted).result()

  protected override def runTest(): Unit = {
    series = longSeriesTest(
      stream.head, stream.head, 0, stream.tail,
      mutable.Map(1 -> 0, 2 -> 0, 3 -> 0, 4 -> 0, 5 -> 0, 6 -> 0)
    )
  }

  override protected def check(): Unit = {
    assert(series(1) >= 2343 && series(1) <= 2657)
    assert(series(2) >= 1135 && series(2) <= 1365)
    assert(series(3) >= 542 && series(3) <= 708)
    assert(series(4) >= 251 && series(4) <= 373)
    assert(series(5) >= 111 && series(5) <= 201)
    assert(series(6) >= 111 && series(6) <= 201)
  }

  @tailrec
  private def longSeriesTest(previous: Boolean, next: Boolean, n: Int, stream: LazyList[Boolean], result: mutable.Map[Int, Int]): Map[Int, Int] = {
    if (stream.isEmpty) result.toMap
    else {
      if (next == previous) longSeriesTest(next, stream.head, n+1, stream.tail, result)
      else {
        if (n >= 1 && n <= 6) {
          result(n) = result(n) + 1
          longSeriesTest(next, stream.head, 1, stream.tail, result)
        }
        else {
          result(6) = result(6) + 1
          longSeriesTest(next, stream.head, 1, stream.tail, result)
        }
      }
    }
  }
}
//
//object SeriesTest {
//  def apply(stream: LazyList[Boolean]): Boolean = new SeriesTest(stream).test()
//}
