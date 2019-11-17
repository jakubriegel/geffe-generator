package eu.jrie.put.pod.geffe.generator

import eu.jrie.put.pod.geffe.registry.{Fibonacci, Xor}
import org.scalatest.FunSuite

import scala.collection.mutable

class GeneratorTest extends FunSuite {

  private val N = 20_000

  test("should pass series test") {
    // when
    val stream = generator().get()

    // then
    val series = mutable.Map(
      1 -> 0,
      2 -> 0,
      3 -> 0,
      4 -> 0,
      5 -> 0,
      6 -> 0,
    )
    var previous = stream.head
    var n = 0
    stream foreach { b => {
      if (b == previous) n += 1
      else {
        if (n >= 1 && n <= 6) series(n) = series(n)+1
        else series(6) = series(6)+1
        n = 1
      }
      previous = b
    } }

    println(series)
    assert(series(1) >= 2343 && series(1) <= 2657)
    assert(series(2) >= 1135 && series(2) <= 1365)
    assert(series(3) >= 542 && series(3) <= 708)
    assert(series(4) >= 251 && series(4) <= 373)
    assert(series(5) >= 111 && series(5) <= 201)
    assert(series(6) >= 111 && series(6) <= 201)
  }

  test("should pass long series test") {
    // when
    val stream = generator().get()

    // then
    var previous = stream.head
    var n = 0
    var noLongSeries = true
    stream foreach { b => {
      if (b == previous) n += 1
      else {
        if (n >= 26) noLongSeries = false
        else n = 1
      }
      previous = b
    } }
    assert(noLongSeries)
  }

  test("should pass poker test") {
    // when
    val stream = generator().get().toList

    // then
    val s: Double = stream.sliding(4, 4).toList
      .groupBy(identity).view
      .mapValues(_.size).values
      .map(i => { println(s"$i"); i })
      .map(i => i*i)
      .map(i => i.toDouble)
      .sum

    val testResult = (16.0/5000.0 * s) - 5000.0
    println(testResult)
    assert(testResult > 2.16)
    assert(testResult < 46.17)
  }

  test("should pass monobit test") {
    // when
    val stream = generator().get()

    // then
    val n = stream.count(i => i)
    println(n)
    assert(n > 9725)
    assert(n < 10275)
  }

  private def generator() = new Generator(
    N,
    new Fibonacci(
      List(true, false, true, true, true, false, true, true, true, true, true, false, true, true, true, false),
    ),
    new Xor(
      List(false, true, true, true, false, true, false, true, false, true, false, true, false, true, false, true),
      List(true, true, false, true, false, true, true, true, true, true, true, false, true, false, true, false),
    ),
    new Xor(
      List(true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false),
      List(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true),
    )
  )
}
