package eu.jrie.put.pod.geffe.user

import eu.jrie.put.pod.geffe.generator.Generator
import eu.jrie.put.pod.geffe.registry.{Fibonacci, LFSR, Xor}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

class GeffeGUI extends JFXApp {

  import GeffeGUI.LfsrInput

  private val lengthField = new TextField()
  private val lfsrInputs = (new LfsrInput(1), new LfsrInput(2), new LfsrInput(3))
  private val generateButton = new Button("Generate") { onAction = _ => generateAction() }
  private val resultArea = new TextArea() { editable = false; wrapText = true }

  private def generateAction(): Unit = {
    resultArea.clear()
    new Generator(
      lengthField.getText.toInt,
      lfsrInputs._1.registry, lfsrInputs._2.registry, lfsrInputs._3.registry
    ).get()
      .map(b => if(b) 1 else 0) foreach { b => resultArea.appendText(b.toString) }
  }

  stage = new PrimaryStage {
    resizable = false
    title = "Geffe Generator"
    scene = new Scene {
      content = new VBox {
        padding = Insets(25)
        children = Seq(
          new HBox(
            new Label("Length"), lengthField
          ),
          lfsrInputs._1,
          lfsrInputs._2,
          lfsrInputs._3,
          generateButton,
          resultArea
        )
      }
    }
  }


  def start(): Unit = main(Array())

}

object GeffeGUI {

  object LFSRType extends Enumeration {
    val XOR, FIBONACCI = Value
  }

  import LFSRType._

  class LfsrInput (lfsrId: Int) extends VBox {

    private val typeCombo = new ComboBox(Seq(XOR, FIBONACCI)) { value = XOR }
    private val sizeField = new TextField()
    private val initialStateField = new TextField()
    private val coefficientsField = new TextField()

    padding = Insets(10, 0, 10, 0)
    children = Seq(
      new Label(s"LFSR $lfsrId"),
      new HBox(
        new Label("Type"), typeCombo,
        new Label("Size"), sizeField
      ),
      new Label("Initial state"), initialStateField,
      new Label("Coefficients"), coefficientsField
    )

    def registry: LFSR = {
      val registrySize = sizeField.getText.toInt
      typeCombo.getValue match {
        case XOR => new Xor(initialStateField.getText, coefficientsField.getText)
        case FIBONACCI => new Fibonacci(initialStateField.getText)
      }
    }

    private implicit def toRegistry(t: String): List[Boolean] = t.toCharArray
      .map(b => if (b == '1') true else false)
      .toList

  }
}
