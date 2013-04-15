package services

import scala.collection.mutable.ArrayBuffer
import model.LogEntry

object LogProcessor {

  def groupByServiceName(timings: ArrayBuffer[LogEntry]) : List[(String, ArrayBuffer[LogEntry])] = {
    val timingsGroupedByServiceName : List[(String, ArrayBuffer[LogEntry])] = timings.groupBy(_.serviceName).toList

    timingsGroupedByServiceName.foreach{
      case (service, lst) => {
        val skew = 60 - lst(0).getMinute()
        lst.foreach{
          item => item.setMinute((item.getMinute() + skew) % 60)
        }
      }
    }

    timingsGroupedByServiceName
  }

}
