import akka.actor.{Props, Actor, ActorSystem}
import controllers.Application
import java.util.concurrent.TimeUnit
import play.api._
import play.api.Application
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import services.Crawler

object Global extends GlobalSettings {

  val system = ActorSystem("Akka")

  override def onStart(app: Application) {
    Logger.info("Application has started")

    val timerActor = system.actorOf(Props[TimerActor], name = "timeractor")
    system.scheduler.schedule(
      Duration.create(10, TimeUnit.SECONDS),
      Duration.create(7, TimeUnit.MINUTES),
      timerActor,
      "tick"
    )

    var index = 0
    Application.generateNDates(4).tail.foreach { date =>
      system.scheduler.scheduleOnce(
        Duration.create(index + 1, TimeUnit.MINUTES),
        timerActor,
        date
      )
      index = index + 10
    }

  }
}

class TimerActor extends Actor {

  def receive = {
    case "tick" => {
      Logger.debug("Incremental update message received")
      try {
        Crawler.incrementalUpdate()
      } catch {
        case e: Exception => Logger.error("Error in incremental update!")
      }
    }
    case date: String => {
      Logger.debug("One-off update message received for date " + date)
      try {
        Crawler.update(date)
      } catch {
        case e: Exception => Logger.error("Error in one-off update!")
      }
    }
  }
}
