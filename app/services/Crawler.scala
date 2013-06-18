package services

import scala.collection.mutable.ArrayBuffer
import model.LogEntry
import play.Logger

object Crawler {

  val legs = List("a", "b")
  val servers = List("03", "04", "05", "06", "07", "08")
  val baseUrl = "http://logs.ocp.bskyb.com/node_logs/chiocp<leg>app<server>.bskyb.com/ocp_tomcat/"
  val map: scala.collection.mutable.Map[String, Boolean] = scala.collection.mutable.Map()

  def incrementalUpdate() {
    legs.foreach { leg =>
      servers.foreach { server =>
        val timings: ArrayBuffer[LogEntry] = ArrayBuffer[LogEntry]()
        val urls: List[String] = HtmlCrawler.extractLogFileNames(baseUrl.replaceFirst("<leg>", leg).replaceFirst("<server>", server))
        urls.foreach { url =>
          if (!map.contains(url)) {
            map.put(url, true)
            timings.appendAll(LogParser.extractTimingsFromUrl(baseUrl.replaceFirst("<leg>", leg).replaceFirst("<server>", server) + url, leg, server, ""))
          }
        }
        if (timings.size > 0) {
          LogEntry.create(timings)
        }
      }
    }
  }

  def update(range: String) {
    val tarFileName = "_archive/" + range + ".tar.gz"
    val timings: ArrayBuffer[LogEntry] = ArrayBuffer[LogEntry]()
    legs.foreach { leg =>
      servers.foreach { server =>
        if (!map.contains(tarFileName + "-" +leg + "-" + server)) {
          map.put(tarFileName + "-" + leg + "-" + server, true)
          try {
            timings.appendAll(LogParser.extractTimingsFromGzipUrl(baseUrl.replaceFirst("<leg>", leg).replaceFirst("<server>", server) + tarFileName, leg, server, ""))
          } catch {
            case e: Exception => Logger.error("Error in reading tar file " + tarFileName)
          }
        }
      }
    }
    if (timings.size > 0) {
      LogEntry.create(timings)
    }
  }

}
