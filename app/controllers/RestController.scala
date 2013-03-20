package controllers

import play.api.mvc._
import services.LogParser
import model.LogEntry
import collection.mutable.ArrayBuffer

object RestController extends Controller {
  
  def timings (source: String) = Action {
    val timings: ArrayBuffer[LogEntry] = LogParser.extractTimingsFromUrl(source)
    val timingsGroupedByServiceName: List[(String, ArrayBuffer[LogEntry])] = timings.groupBy(_.serviceName).toList

    timingsGroupedByServiceName.foreach{
      case (service, lst) => {
        val skew = 60 - lst(0).getMinute()
        lst.foreach{
          item => item.setMinute((item.getMinute() + skew) % 60)
        }
      }
    }

    Ok(views.txt.timings(timingsGroupedByServiceName.toList))
  }

}