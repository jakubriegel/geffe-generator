package eu.jrie.put.pod.geffe.user

import com.sandec.mdfx.MDFXNode
import eu.jrie.put.pod.geffe.cipher.Cipher
import eu.jrie.put.pod.geffe.generator.Generator
import eu.jrie.put.pod.geffe.registry.{Fibonacci, LFSR, Xor}
import javafx.scene.Node
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}
import scalafx.stage.FileChooser

import scala.io.{Codec, Source}
import scala.reflect.io.File
import scala.util.Random

class GeffeGUI extends JFXApp {

  import GeffeGUI.LfsrInput

  private val lengthField = new TextField() { onKeyReleased = _ => randomRegistersAction() }
  private val randomRegistersCheckbox = new CheckBox("random registers")
  private val lfsrInputs = (new LfsrInput(1), new LfsrInput(2), new LfsrInput(3))
  private val generateButton = new Button("Generate") { onAction = _ => generateAction() }
  private val generateToFileCheckbox = new CheckBox("to file")
  private val resultArea = new TextArea() { editable = false; wrapText = true }

  private def randomRegistersAction(): Unit = if (randomRegistersCheckbox.isSelected) {
    lfsrInputs._1.random()
    lfsrInputs._2.random()
    lfsrInputs._3.random()
  }

  private def generateAction(): Unit = {
    resultArea.clear()
    printStream(getStream)
  }

  private def getStream =  new Generator(
    lengthField.getText.toInt,
    lfsrInputs._1.registry, lfsrInputs._2.registry, lfsrInputs._3.registry
  ).get()

  private val printToResultsArea = (stream: LazyList[Boolean]) => {
    stream
      .map(b => if(b) "1" else "0")
      .foreach(resultArea.appendText)
  }
  private val printToFile = (stream: LazyList[Boolean]) => {
    val file = File(s"${System.getProperty("user.dir")}/stream")
    if (file.exists) file.delete()
    val writer = file.printWriter()
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


  private def streamTab = new Tab {
    text = "stream"
    closable = false
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

  private val dataButton = new Button("Data source") { onAction = _ => dataSourceAction() }
  private val dataLabel = new Label("")
  private val generatedStreamCheckbox = new CheckBox("Generate stream")
  private val streamButton = new Button("Stream source") { onAction = _ => streamSourceAction() }
  private val streamLabel = new Label()
  private val codeButton = new Button("Code") { onAction = _ => codeAction() }

  private var dataFile: File = _
  private var streamFile: File = _

  private def dataSourceAction(): Unit = {
    dataFile = new File(new FileChooser().showOpenDialog(dataButton.getScene.getWindow))
    dataLabel.text = dataFile.toCanonical.path
  }

  private def streamSourceAction(): Unit = {
    streamFile = new File(new FileChooser().showOpenDialog(streamButton.getScene.getWindow))
    streamLabel.text = streamFile.toCanonical.path
  }

  private def codeTab = new Tab {
    text = "code"
    closable = false
    content = new VBox {
      padding = Insets(25)
      children = Seq(
        new VBox(
          new Label("Data"),
          new HBox(
            dataButton, dataLabel
          )
        ),
        new VBox {
          padding = Insets(10, 0, 10, 0)
          children = Seq(
            new Label("Stream"),
            new VBox(
              generatedStreamCheckbox,
              new HBox(
                streamButton, streamLabel
              )
            )

          )
        },
        new HBox(
          codeButton
        )
      )
    }
  }

  private def codeAction(): Unit = {
    val dataSource = Source.fromFile(dataFile.path, Codec.UTF8.name)
    val data = LazyList.fill(dataFile.length.toInt) {
      dataSource.next()
    } flatMap toBits

    val stream = if (generatedStreamCheckbox.isSelected) getStream
    else {
      val streamSource = Source.fromFile(streamFile.path, Codec.UTF8.name)
      LazyList.fill(streamFile.length.toInt) {
        streamSource.next()
      } filter { b => b == '1' || b == '0' } map toBoolean
    }

    val resultWriter = File(s"$dataFile.enc").printWriter()
    Cipher.code(stream, data) sliding(8, 8) map {it => it.reverse} map { b => {
      var byte = 0
      b foreach { i =>
        byte = byte << 1
        byte = byte ^ (if (i) 1 else 0)
      }
      byte
    } } map { b => b.toChar } foreach resultWriter.print

    dataSource.close()
    resultWriter.flush()
    resultWriter.close()
    println("done")
  }

  private def toBits(from: Char) = 0 to 7 map { i => if (((from >> i) & 1) == 1) true else false }
  private def toBoolean(from: Char) = if(from == '1') true else false

  private def aboutTab = {
    val aboutFile = GeffeGUI.getClass.getResource("/about.md")
    val aboutFileSource = Source.fromFile(aboutFile.toURI)
    val aboutText = aboutFileSource.getLines.mkString("\n")
    aboutFileSource.close()
    val aboutNode: Node = new MDFXNode(/*aboutText*/"see github repo") {
      maxWidth(100.0)
      prefWidth(100)
    }

    new Tab {
      text = "about"
      closable = false

      content = new ScrollPane {
        padding = Insets(25)
        content = new VBox { children.addAll(aboutNode) }
      }
    }
  }

  stage = new PrimaryStage {
    resizable = true
    title = "Geffe Generator"
    scene = new Scene {
      content = new TabPane {
        this += streamTab
        this += codeTab
        this += aboutTab
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
      typeCombo.getValue match {
        case XOR => new Xor(initialStateField.getText, coefficientsField.getText)
        case FIBONACCI => new Fibonacci(initialStateField.getText)
      }
    }

    def random(): Unit = {
      val random = new Random()
      val size = random.nextInt(63) + 16
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
