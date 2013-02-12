package services

import model.LogEntry
import java.util
import io.{BufferedSource, Source}

object LogParser {

  def extractTimingsFromUrl(fileUrl: String) = {
    extractTimings(Source.fromURL(fileUrl))
  }

  def extractTimings(source: BufferedSource) = {
    val list: util.ArrayList[LogEntry] = new util.ArrayList[LogEntry]()
    source.getLines() foreach { line =>
      if (line.contains("took")) {
        println(line)
        val M = """.* \[\d+ \S+ \d+ (\S+)] .* (\S+) took (\d+).*""".r
        val M (timestamp, serviceName, duration) = line
        list.add(new LogEntry(serviceName, timestamp, duration.toLong))
      }
    }
    list
  }
}
