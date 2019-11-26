package eu.jrie.put.pod.geffe.user

import eu.jrie.put.pod.geffe.generator.Generator
import eu.jrie.put.pod.geffe.registry.{Fibonacci, LFSR, Xor}
import eu.jrie.put.pod.geffe.test.FIPS

import scala.io.Source
import scala.reflect.io.File

object GeffeCLI {
  def start(args: Array[String]): Unit = {
    args.head.toLowerCase match {
      case "stream" => stream(args.tail)
      case "test" => test(args.tail)
    }
  }

  private def stream(args: Array[String]): Unit = {
    val n = args.head.toInt
    val lfsr = generateRegistries(args.tail)

    new Generator(n, lfsr(0), lfsr(0), lfsr(0))
      .get()
      .map(b => if(b) 1 else 0)
      .foreach(print)

    println()
  }

  private def generateRegistries(args: Array[String]): List[LFSR] = args.length match {
    case 0 => Nil
    case _ => registryFromArgs(args.head, args.tail)
  }

  private def registryFromArgs(registryType: String, args: Array[String]): List[LFSR] =
    registryFromArgs(registryType, args.head.toInt, args.tail)

  private def registryFromArgs(registryType: String, registrySize: Int, args: Array[String]): List[LFSR] =
    (registryType.toLowerCase() match {
      case "xor" => new Xor(LFSR.randomRegistry(registrySize), LFSR.randomRegistry(registrySize))
      case "fib" => new Fibonacci(LFSR.randomRegistry(registrySize))
    }) :: generateRegistries(args)

  private def test(args: Array[String]): Unit = new FIPSRunner(args.head).test()

  private class FIPSRunner (streamFileName: String) {
    private val streamFile = File(streamFileName)

    def test(): Unit = {
      executeTest("series test", FIPS.withInfo.seriesTest)
      executeTest("long series test", FIPS.withInfo.longSeriesTest)
      executeTest("poker test", FIPS.withInfo.pokerTest)
      executeTest("monobit test", FIPS.withInfo.monobitTest)
    }

    private def executeTest(name: String, t: LazyList[Boolean] => (Boolean, String)): Unit = {
      val (result, log) = fips { s => t(s) }
      println(s"Test: $name - ${if (result) "PASSED" else "FAILED" } ")
      if (log.nonEmpty) println(log)
      println()
    }

    private def fips(test: LazyList[Boolean] => (Boolean, String)): (Boolean, String) = {
      val streamSource = Source.fromFile(streamFileName)
      val stream = LazyList.fill(streamFile.length.toInt) {
        streamSource.next()
      } map { b => if(b == '1') true else false }
      test(stream)
    }
  }

}
