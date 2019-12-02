package eu.jrie.put.pod.geffe.test

import scala.annotation.tailrec
import scala.collection.immutable.ListMap
import scala.collection.mutable

private class SeriesTest(override protected val stream: LazyList[Boolean]) extends TestFIPS {

  private var series: Map[String, Int] = _
  def result: Map[String, Int] = ListMap.newBuilder.addAll(series.toSeq.sorted).result()

  protected override def runTest(): Unit = {
    series = longSeriesTest(
      stream.head, stream.head, 0, stream.tail,
      mutable.Map(
        "0_1" -> 0, "0_2" -> 0, "0_3" -> 0, "0_4" -> 0, "0_5" -> 0, "0_6" -> 0,
        "1_1" -> 0, "1_2" -> 0, "1_3" -> 0, "1_4" -> 0, "1_5" -> 0, "1_6" -> 0
      )
    )
    log(result map {
      case (k, v) => s"$k - $v"
    } mkString "\n")
  }

  override protected def check(): Unit = {
    assert(series("0_1") >= 2343 && series("0_1") <= 2657)
    assert(series("0_2") >= 1135 && series("0_2") <= 1365)
    assert(series("0_3") >= 542 && series("0_3") <= 708)
    assert(series("0_4") >= 251 && series("0_4") <= 373)
    assert(series("0_5") >= 111 && series("0_5") <= 201)
    assert(series("0_6") >= 111 && series("0_6") <= 201)

    assert(series("1_1") >= 2343 && series("1_1") <= 2657)
    assert(series("1_2") >= 1135 && series("1_2") <= 1365)
    assert(series("1_3") >= 542 && series("1_3") <= 708)
    assert(series("1_4") >= 251 && series("1_4") <= 373)
    assert(series("1_5") >= 111 && series("1_5") <= 201)
    assert(series("1_6") >= 111 && series("1_6") <= 201)
  }

  @tailrec
  private def longSeriesTest(previous: Boolean, next: Boolean, n: Int, stream: LazyList[Boolean], result: mutable.Map[String, Int]): Map[String, Int] = {
    if (stream.isEmpty) result.toMap
    else {
      if (next == previous) longSeriesTest(next, stream.head, n+1, stream.tail, result)
      else {
        if (n >= 1 && n <= 6) {
          increment(next, n, result)
          longSeriesTest(next, stream.head, 1, stream.tail, result)
        }
        else {
          increment(next, 6, result)
          longSeriesTest(next, stream.head, 1, stream.tail, result)
        }
      }
    }
  }

  private def increment(next: Boolean, n: Int, result: mutable.Map[String, Int]): Unit = {
    if (next) result(s"1_$n") = result(s"1_$n") + 1
    else result(s"0_$n") = result(s"0_$n") + 1
  }

}
