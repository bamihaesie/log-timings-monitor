package model

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import scala.collection.mutable.ArrayBuffer
import org.joda.time.DateTime
import services.AnormExtension._

class LogEntry (var serviceName: String,
                var timestamp: DateTime,
                var duration: Double,
                var leg: String,
                var server: String) {

  override def toString: String = serviceName + " -> " + duration

  def getTime() = {
    timestamp.getMillis();
  }

  def getLegAndServer() = leg + " " + server

}

object LogEntry {

  def all(): List[LogEntry] = DB.withConnection { implicit c =>
    SQL("select * from log_entry").as(logEntry *)
  }

  def find(range: String, services: String): List[LogEntry] = {
    if (!"".equals(services)) {
      findByRangeAndServices(range, services)
    } else {
      findByRange(range)
    }
  }

  def findByRange(range: String) = DB.withConnection { implicit c =>
    SQL(
      """
        select *
        from log_entry
        where created_on >= {range}
          and created_on < dateadd('DAY', 1, {range})
      """
    )
      .on("range" -> range)
      .as(logEntry *)
  }

  def findByRangeAndServices(range: String, services: String) = DB.withConnection { implicit c =>
    println(services)
    SQL(
      """
        select *
        from log_entry
        where created_on >= {range}
          and created_on < dateadd('DAY', 1, {range})
          and service_name IN (
      """ + services + ")"
    )
      .on("range" -> range)
      .as(logEntry *)
  }

  def create(list: ArrayBuffer[LogEntry]) {
    DB.withConnection { implicit c =>
      val insertQuery = SQL("insert into log_entry (service_name, duration, leg, server, created_on)" +
        " values ({serviceName}, {duration}, {leg}, {server}, {timestamp})")

      val batchInsert = (insertQuery.asBatch /: list)(
        (sql, elem) => sql.addBatchParams(elem.serviceName,
                                          elem.duration,
                                          elem.leg,
                                          elem.server,
                                          elem.timestamp)
      )

      batchInsert.execute()

    }
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from log_entry where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }

  val logEntry = {
    get[String]("service_name") ~
    get[Double]("duration") ~
    get[String]("leg") ~
    get[String]("server") ~
    get[DateTime]("created_on") map {
      case service_name~duration~leg~server~created_on => new LogEntry(service_name, created_on, duration, leg, server)
    }
  }

}
