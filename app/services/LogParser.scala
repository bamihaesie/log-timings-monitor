package services

import model.LogEntry
import java.util
import io.{BufferedSource, Source}
import collection.mutable
import java.util.zip.GZIPInputStream
import java.io.FileInputStream
import java.net.URL

object LogParser {

  def extractTimingsFromUrl(fileUrl: String, leg: String, server: String, services: String) = {
    extractTimings(Source.fromURL(fileUrl), leg, server, services)
  }

  def extractTimingsFromGzipUrl(gzipFileUrl: String, leg: String, server: String, services: String) = {
    val stream = new GZIPInputStream(new URL(gzipFileUrl).openStream())
    val source: BufferedSource = Source.fromInputStream(stream)
    extractTimings(source, leg, server, services)
  }

  def extractTimings(source: BufferedSource, leg: String, server: String, services: String) = {
    val list: mutable.ArrayBuffer[LogEntry] = new mutable.ArrayBuffer[LogEntry]()
    val selectedServices = services.split('|')
    source.getLines() foreach { line =>
      if (line.contains("took")) {
        try {
          val M = """.* \[(\d+ \S+ \d+ \S+)] .* (\S+) took ([\d,]+)ms.*""".r
          val M (timestamp, serviceName, duration) = line
          if ("".equals(services) || isSelected(serviceName.replaceAll(",", ""), selectedServices)) {
            val entry = new LogEntry(serviceName.replaceAll(",", ""), timestamp, leg, server,
                                  duration.replaceAll(",", "").toDouble / 1000)
            list.+=(entry)
          }
        } catch {
          case e : Exception =>
        }
      }
    }
    list
  }

  def isSelected(serviceName: String, services: Array[String]) : Boolean = {
    services.foreach { service =>
      if (serviceName.equals(service)) {
        return true;
      }
    }
    return false;
  }
}
