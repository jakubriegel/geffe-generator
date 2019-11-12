package eu.jrie.put.pod.geffe

import eu.jrie.put.pod.geffe.generator.Generator
import eu.jrie.put.pod.geffe.registry.{Fibonacci, LFSR, Xor}

object Main {
  def main(args: Array[String]): Unit = {
    args.length match {
      case 0 => gui()
      case _ => cli(args)
    }
  }

  private def gui(): Unit = {}

  private def cli(args: Array[String]): Unit = {
    val n = args.head.toInt
    val lfsr = generateRegistries(args.tail)

    new Generator(n, lfsr(0), lfsr(0), lfsr(0))
      .get()
      .map(b => if(b) 1 else 0)
      .foreach(print)
  }

  private def generateRegistries(args: Array[String]): List[LFSR] = args.length match {
    case 0 => Nil
    case _ => registryFromArgs(args.head, args.tail)
  }

  private def registryFromArgs(registryType: String, args: Array[String]): List[LFSR]
    = registryFromArgs(registryType, args.head.toInt, args.tail)

  private def registryFromArgs(registryType: String, registrySize: Int, args: Array[String]): List[LFSR]
    = (registryType.toLowerCase() match {
      case "xor" => new Xor(LFSR.randomRegistry(registrySize), LFSR.randomRegistry(registrySize))
      case "fib" => new Fibonacci(LFSR.randomRegistry(registrySize))
    }) :: generateRegistries(args)

}
