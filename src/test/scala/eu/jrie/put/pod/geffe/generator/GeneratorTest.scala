package eu.jrie.put.pod.geffe.generator

import eu.jrie.put.pod.geffe.registry.{Fibonacci, Xor}
import org.scalatest.FunSuite

class GeneratorTest extends FunSuite {

  private val N = 15

  test("should generate stream without long series") {
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
      .map(i => i.toLong)
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
    20_000,
    new Fibonacci(
      List(true, false, true, true, true, true, true, true, true, true, true, false, true, true, true, false),
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
