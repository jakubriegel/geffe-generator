package eu.jrie.put.pod.geffe

import eu.jrie.put.pod.geffe.user.{GeffeCLI, GeffeGUI}

object Main {
  def main(args: Array[String]): Unit = {
    args.length match {
      case 0 => gui()
      case _ => cli(args)
    }
  }

  private def gui(): Unit = new GeffeGUI().start()
  private def cli(args: Array[String]): Unit = GeffeCLI.start(args)
}
