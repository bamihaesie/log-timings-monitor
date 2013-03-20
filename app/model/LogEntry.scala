package model

class LogEntry (var serviceName: String, var timestamp: String, var duration: Long, var minute: Integer) {

  computeMinute()

  def this(serviceName: String, timestamp: String, duration: Long) = this(serviceName, timestamp, duration, 0)

  override def toString: String = serviceName + " -> " + duration + " @ " + timestamp

  def computeMinute() = {
    val min: String = timestamp.split(":")(1)
    if (min.charAt(0) == '0') {
      minute = min.substring(1).toInt
    } else {
      minute = min.toInt
    }
  }

  def getMinute() = minute
  def setMinute(newMinute: Integer) {
    minute = newMinute
  }

}
