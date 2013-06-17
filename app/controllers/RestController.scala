package controllers

import play.api.mvc._
import services.{LogProcessor, HtmlCrawler, LogParser}
import model.LogEntry
import collection.mutable.ArrayBuffer
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
import java.sql.Timestamp

object RestController extends Controller {

  def timings (range: String, services: String) = Action {
    val timings = LogEntry.find(range, services)
    val timingsGroupedByServiceName = LogProcessor.groupByServiceName(timings)
    Ok(views.txt.timings(timingsGroupedByServiceName))
  }

}
