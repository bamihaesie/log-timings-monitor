package controllers

import play.api.mvc._
import services.LogProcessor
import model.LogEntry

object RestController extends Controller {

  def timings (range: String, services: String) = Action {
    val timings = LogEntry.find(range, services)
    val timingsGroupedByServiceName = LogProcessor.groupByServiceName(timings)
    Ok(views.txt.timings(timingsGroupedByServiceName))
  }

}
