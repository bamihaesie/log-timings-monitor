package services

import scala.collection.mutable.ArrayBuffer
import model.LogEntry

object LogProcessor {

  def groupByServiceName(timings: ArrayBuffer[LogEntry]) = {

    val timingsGrouped = timings.groupBy(_.serviceName).mapValues(_.groupBy(_.getLegAndServer()).toList).toList

    timingsGrouped.foreach{
      case (service, rest) => {
        rest.foreach{
          case (legAndServer, lst) =>
            lst.foreach{
//              val skew =  240000// - lst(0).getTime()
//              item => item.setTime((item.getTime() + skew) % 240000)
                item => item.setTime(item.getTime())
            }
        }
      }
    }

    timingsGrouped
  }

}
