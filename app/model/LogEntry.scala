package model

import java.util.Date

class LogEntry (var serviceName: String,
                var timestamp: String,
                var duration: Double,
                var leg: String,
                var server: String,
                var time: Long) {

  computeTime()

  def this(serviceName: String, timestamp: String, leg: String, server: String, duration: Double) =
    this(serviceName, timestamp, duration, leg, server, 0)

  override def toString: String = serviceName + " -> " + duration + " @ " + timestamp

  def computeTime() = {
    val format = new java.text.SimpleDateFormat("dd MMM yyyy HH:mm:ss")
    val date: Date = format.parse(timestamp)
    time = date.getTime
  }

  def getTime() = time
  def setTime(newTime: Long) {
    time = newTime
  }

  def getLegAndServer() = leg + " " + server

}
