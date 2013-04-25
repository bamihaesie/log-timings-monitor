package controllers

import play.api.mvc._
import services.{LogProcessor, Crawler, LogParser}
import model.LogEntry
import collection.mutable.ArrayBuffer

object RestController extends Controller {

  val legs = List("a", "b")
  val servers = List("03", "04", "05", "06", "07", "08")
  val baseUrl = "http://logs.ocp.bskyb.com/node_logs/chiocp<leg>app<server>.bskyb.com/ocp_tomcat/"

  def timings (range: Integer) = Action {

    val timings: ArrayBuffer[LogEntry] = new ArrayBuffer[LogEntry]()

    legs.foreach { leg =>
      servers.foreach { server =>
        val urls: List[String] = Crawler.extractLogFileNames(baseUrl.replaceFirst("<leg>", leg).replaceFirst("<server>", server))
        urls.foreach{ url =>
          timings.appendAll(LogParser.extractTimingsFromUrl(baseUrl.replaceFirst("<leg>", leg).replaceFirst("<server>", server) + url, leg,server))
        }
      }
    }

    val timingsGroupedByServiceName = LogProcessor.groupByServiceName(timings)

    Ok(views.txt.timings(timingsGroupedByServiceName.toList))
  }

}
