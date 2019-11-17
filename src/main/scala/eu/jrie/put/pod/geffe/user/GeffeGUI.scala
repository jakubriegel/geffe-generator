package eu.jrie.put.pod.geffe.user

import java.io.{File, PrintWriter}

import com.sandec.mdfx.MDFXNode
import eu.jrie.put.pod.geffe.generator.Generator
import eu.jrie.put.pod.geffe.registry.{Fibonacci, LFSR, Xor}
import javafx.scene.Node
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

import scala.io.Source
import scala.util.Random

class GeffeGUI extends JFXApp {

  import GeffeGUI.LfsrInput

  private val lengthField = new TextField() { onKeyReleased = _ => randomRegistersAction() }
  private val randomRegistersCheckbox = new CheckBox("random registers")
  private val lfsrInputs = (new LfsrInput(1), new LfsrInput(2), new LfsrInput(3))
  private val generateButton = new Button("Generate") { onAction = _ => generateAction() }
  private val generateToFileCheckbox = new CheckBox("to file")
  private val resultArea = new TextArea() { editable = false; wrapText = true }

  private def streamTab = new Tab {
    text = "stream"
    content = new VBox {
      padding = Insets(25)
      children = Seq(
        new HBox(
          new Label("Length"), lengthField,
          randomRegistersCheckbox
        ),
        lfsrInputs._1, lfsrInputs._2, lfsrInputs._3,
        new HBox(
          generateButton, generateToFileCheckbox
        ),
        resultArea
      )
    }
  }

  private def aboutTab = {
    val aboutFile = GeffeGUI.getClass.getResource("/about.md")
    val aboutFileSource = Source.fromFile(aboutFile.toURI)
    val aboutText = aboutFileSource.getLines.mkString
    aboutFileSource.close()
    val aboutNode: Node = new MDFXNode(aboutText)

    new Tab {
      text = "about"
      content = new ScrollPane {
        content = new VBox { children.addAll(aboutNode) }
      }
    }
  }

  stage = new PrimaryStage {
    resizable = false
    title = "Geffe Generator"
    scene = new Scene {
      content = new TabPane {
        this += streamTab
        this += aboutTab
      }
    }
  }

  def start(): Unit = main(Array())

  private def randomRegistersAction(): Unit = if (randomRegistersCheckbox.isSelected) {
    lfsrInputs._1.random()
    lfsrInputs._2.random()
    lfsrInputs._3.random()
  }

  private def generateAction(): Unit = {
    resultArea.clear()
    printStream(
      new Generator(
        lengthField.getText.toInt,
        lfsrInputs._1.registry, lfsrInputs._2.registry, lfsrInputs._3.registry
      ).get()
    )
  }

  private val printToResultsArea = (stream: LazyList[Boolean]) => {
    stream
      .map(b => if(b) "1" else "0")
      .foreach(resultArea.appendText)
  }
  private val printToFile = (stream: LazyList[Boolean]) => {
    val file = new File(s"${System.getProperty("user.dir")}/stream")
    if (file.exists()) file.delete()
    val writer = new PrintWriter(file)
    stream
      .map(b => if(b) '1' else '0')
      .sliding(1024, 1024)
      .foreach(chunk => {
        chunk foreach { b => writer.append(b) }
        writer.flush()
      })
  }

  private def printStream(stream: LazyList[Boolean]): Unit = if (generateToFileCheckbox.isSelected) {
    printToFile(stream)
  }
  else {
    printToResultsArea(stream)
  }

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

    def random(): Unit = {
      val random = new Random()
      val size = random.nextInt(31) + 1
      sizeField.setText(size.toString)
      initialStateField.setText("")
      coefficientsField.setText("")
      0 until size foreach {
        _ -> {
          initialStateField.appendText(if (random.nextBoolean()) "1" else "0")
          coefficientsField.appendText(if (random.nextBoolean()) "1" else "0")
        }
      }

      val t = LFSRType(random.nextInt(2))
      typeCombo.setValue(t)
      if (t == FIBONACCI) coefficientsField.setText("")
    }

    private implicit def toRegistry(t: String): List[Boolean] = t.toCharArray
      .map(b => if (b == '1') true else false)
      .toList

  }
}
