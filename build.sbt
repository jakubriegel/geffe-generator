name := "geffe"

version := "0.1"

scalaVersion := "2.13.0"

resolvers += "sandec" at "http://sandec.bintray.com/repo"

// gui
libraryDependencies += "org.scalafx" %% "scalafx" % "12.0.2-R18"
val javafxModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}
libraryDependencies ++= javafxModules.map(m => "org.openjfx" % s"javafx-$m" % "12.0.2" classifier osName)
libraryDependencies += "com.sandec" % "mdfx" % "0.1.6"

// test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"
