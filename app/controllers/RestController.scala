package controllers

import play.api.mvc._
import services.LogParser
import io.BufferedSource
import java.io.{File, FileInputStream}
import model.LogEntry
import play.api.libs.json._
import collection.mutable.ArrayBuffer

object RestController extends Controller {
  
  def timings = Action {
    val source: BufferedSource = new BufferedSource(new FileInputStream(new File("test/resources/tomcat-ocp.log.txt")))
    val timings: ArrayBuffer[LogEntry] = LogParser.extractTimings(source)

    val pairs = timings.zipWithIndex.map( pair => Seq(pair._2 + 1, pair._1.duration))

    val json = Json.obj("label" -> "test", "data" -> pairs)

    Ok(Json.toJson(json)).as("application/json")
  }

}