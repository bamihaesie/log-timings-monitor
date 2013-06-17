package controllers

import play.api.mvc._
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}

object Application extends Controller {
  
  def index = Action {
    val dates = generateNDates(10)
    val services = getServiceList()
    Ok(views.html.index(dates, services))
  }

  def generateNDates(howMany: Integer) = {
    val fmt = DateTimeFormat.forPattern("yyyy-MM-dd")
    0 to howMany map (i => DateTime.now.minusDays(i).toString(fmt))
  }

  private def getServiceList() = {
    List(
      "set-one-off-payment",
      "set-sky-id",
      "add-address",
      "set-voice-activation-date",
      "get-debt-status",
      "set-prospect-date-of-birth",
      "set-tv-installation-appointment",
      "set-directory-listing-preference",
      "get-voice-activation-calendar",
      "get-liveperson-closer-offer",
      "get-prospect-status",
      "set-marketing-preferences",
      "get-fibre-installation-calendar",
      "get-affiliate-offer",
      "set-preferred-email-address",
      "get-tv-installation-calendar",
      "getAdditionalInfo",
      "get-payment-totals",
      "get-urn-offer",
      "set-one-off-payment-authentication",
      "get-fibre-and-voice-installation-calendar",
      "create-order",
      "get-voice-installation-calendar",
      "get-telephony",
      "getAddresses",
      "set-fibre-installation-appointment",
      "set-prospect-name",
      "getPropertyQuestions",
      "get-choice-of-reward",
      "offerings2",
      "set-recurring-payment",
      "set-tv-personalisation",
      "set-voice-installation-appointment",
      "propertyQuestions/clientAnswers/ui",
      "get-sky-id-username-suggestions",
      "telephony/mac/macnotprovided",
      "payments/billingdetails",
      "add-phone-number",
      "create-debt-status",
      "create-prospect-status",
      "orderStatusTask",
      "getOffering"
    )
  }
  
}