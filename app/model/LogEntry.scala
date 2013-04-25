package model

class LogEntry (var serviceName: String,
                var timestamp: String,
                var duration: Long,
                var leg: String,
                var server: String,
                var time: Integer) {

  computeTime()

  def this(serviceName: String, timestamp: String, leg: String, server: String, duration: Long) =
    this(serviceName, timestamp, duration, leg, server, 0)

  override def toString: String = serviceName + " -> " + duration + " @ " + timestamp

  def computeTime() = {
    val tokens = timestamp.split(":")
    time = (tokens(0) + tokens(1) + tokens(2)).toInt
  }

  def getTime() = time
  def setTime(newTime: Integer) {
    time = newTime
  }

  def getLegAndServer() = leg + " " + server

}
