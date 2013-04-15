package controllers

import play.api.mvc._
import services.{LogProcessor, Crawler, LogParser}
import model.LogEntry
import collection.mutable.ArrayBuffer

object RestController extends Controller {

  val baseUrl = "http://logs.ocp.bskyb.com/node_logs/chiocpaapp05.bskyb.com/ocp_tomcat/"

  def timings (range: Integer) = Action {

    val urls: List[String] = Crawler.extractLogFileNames(baseUrl)
    val timings: ArrayBuffer[LogEntry] = new ArrayBuffer[LogEntry]()

    urls.foreach{ url =>
       timings.appendAll(LogParser.extractTimingsFromUrl(baseUrl + url))
    }

    val timingsGroupedByServiceName = LogProcessor.groupByServiceName(timings)

    Ok(views.txt.timings(timingsGroupedByServiceName.toList))
  }

}