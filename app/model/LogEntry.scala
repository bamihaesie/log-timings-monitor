package model

class LogEntry (var serviceName: String, var timestamp: String, var duration: Long) {

  override def toString: String = serviceName + " -> " + duration + " @ " + timestamp

}
