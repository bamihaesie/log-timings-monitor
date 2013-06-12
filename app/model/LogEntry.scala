package model

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.sql.Timestamp

class LogEntry (var serviceName: String,
                var timestamp: Long,
                var duration: Double,
                var leg: String,
                var server: String) {

  override def toString: String = serviceName + " -> " + duration

  def getLegAndServer() = leg + " " + server

}

object LogEntry {

  def all(): List[LogEntry] = DB.withConnection { implicit c =>
    SQL("select * from log_entry").as(logEntry *)
  }

  def create(serviceName: String, duration: Double, leg: String, server: String, timestamp: Long) {
    DB.withConnection { implicit c =>
      SQL("insert into log_entry (service_name, duration, leg, server, created_on)" +
        " values ({serviceName}, {duration}, {leg}, {server}, {timestamp})").on(
        'serviceName -> serviceName,
        'duration -> duration,
        'leg -> leg,
        'server -> server,
        'timestamp -> new Timestamp(timestamp)
      ).executeUpdate()
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
    get[Long]("created_on") map {
      case service_name~duration~leg~server~created_on => new LogEntry(service_name, created_on, duration, leg, server)
    }
  }

}
