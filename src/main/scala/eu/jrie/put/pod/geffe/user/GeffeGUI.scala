package eu.jrie.put.pod.geffe.user

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

class GeffeGUI extends JFXApp {

  def start(): Unit = main(Array())

  stage = new PrimaryStage {
    resizable = false
    title = "Geffe Generator"
    scene = new Scene {
      content = new VBox {
        padding = Insets(25)
        children = Seq(
          new HBox(
            new Label("length"),
            new TextField()
          ),
          lfsrInput(1),
          lfsrInput(2),
          lfsrInput(3),
          new Button("Generate"),
          new TextArea {
            editable = false
          }
        )
      }
    }
  }

  object LFSRType extends Enumeration {
    type LFSRType = Value
    val XOR, FIBONACCI = Value
  }

  import LFSRType._

  private def lfsrInput(lfsrId: Int): VBox = {
    new VBox {
      padding = Insets(10, 0, 10, 0)
      children = Seq(
        new Label(s"LFSR $lfsrId"),
        new HBox(
          new Label("Type"),
          new ComboBox(Seq(LFSRType.XOR, LFSRType.FIBONACCI)) {
            value = XOR
          },
          new Label("Size"),
          new TextField()
        ),
        new Label("Initial state"),
        new TextField(),
        new Label("Coefficients"),
        new TextField()
      )
    }
  }



}
