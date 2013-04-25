package controllers

import play.api.mvc._
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}

object Application extends Controller {
  
  def index = Action {
    val fmt = DateTimeFormat.forPattern("yyyy-MM-dd")
    val args = 0 to 10 map (i => DateTime.now.minusDays(i).toString(fmt))
    Ok(views.html.index(args))
  }
  
}