package services

import model.LogEntry
import java.util
import io.{BufferedSource, Source}
import collection.mutable

object LogParser {

  def extractTimingsFromUrl(fileUrl: String, leg: String, server: String) = {
    extractTimings(Source.fromURL(fileUrl), leg, server)
  }

  def extractTimings(source: BufferedSource, leg: String, server: String) = {
    val list: mutable.ArrayBuffer[LogEntry] = new mutable.ArrayBuffer[LogEntry]()
    source.getLines() foreach { line =>
      if (line.contains("took")) {
        try {
          val M = """.* \[\d+ \S+ \d+ (\S+)] .* (\S+) took (\d+).*""".r
          val M (timestamp, serviceName, duration) = line
          val entry = new LogEntry(serviceName.replaceAll(",", ""), timestamp, leg, server, duration.toLong)
          list.+=(entry)
        } catch {
          case e : Exception =>
        }
      }
    }
    list
  }
}
