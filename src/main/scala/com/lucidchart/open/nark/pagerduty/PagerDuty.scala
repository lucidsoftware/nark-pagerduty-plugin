package com.lucidchart.open.nark.pagerduty

import com.lucidchart.open.nark.plugins._
import dispatch._, Defaults._

class PagerDuty extends AlertPlugin {
	val name = "PagerDuty"

	val tags = Configuration.getList("pagerduty.tags").toSet

	val fallbackEmails: List[String] = Configuration.getList("pagerduty.fallbackEmail")

	def init() = {

	}

	/**
	 * Handle a change in alert state for a particular target
	 * @param alert the AlertEvent for the Alert that changed
	 * @param whether the alert was successfully handled
	 */
	def alert(alert: AlertEvent): Boolean = {
		alertWithBackoff(3, alert)
	}

	/**
	 * Try a function with an exponential back off
	 * @param tries the number of times to retry
	 * @param alert the AlertEvent to sned to PagerDuty
	 * @param backOff the amount of time (in millis) to backoff by
	 * @param triesDone the number of retries we've already done
	 * @param f the function to call. It returns a Boolean if it succeeded
	 * @return whether the function worked
	 */
	private def alertWithBackoff(tries: Int, alert: AlertEvent, backOff: Int = 10, triesDone: Int = 1): Boolean = {
		val succeeded = sendAlert(alert)

		if (succeeded) {
			true
		}
		else {
			if (triesDone < tries) {
				Thread.sleep(backOff)
				alertWithBackoff(tries, alert, math.pow(backOff, 1.25).toInt, triesDone + 1)
			}
			else {
				false
			}
		}
	}

	/**
	 * Send off the alert to PagerDuty
	 * @param alert the AlertEvent to send to PagerDuty
	 * @return whether PagerDuty acknowleged what was sent
	 */
	private def sendAlert(alert: AlertEvent): Boolean = {
		val action = if ((alert.previous == AlertState.error || alert.previous == AlertState.warn) && alert.current == AlertState.normal) {
			"\"resolve\","
		}
		else {
			"\"trigger\","
		}

		val response = Http(
			url("https://events.pagerduty.com/generic/2010-04-15/create_event.json").POST
				.setBody("{" +
					"\"service_key\": \"" + Configuration.get("pagerduty.api.key") + "\"," +
					"\"event_type\": " + action +
					"\"description\": \"" + alert.name + ": " + alert.server + "\"," +
					"\"incident_key\": \"" + alert.id.toString + "\"," +
					"\"client\": \"Nark\"," +
					"\"detail\": { " +
						"\"last_value\": " + "\"" + alert.lastValue + "\"" +
					"}" +
				"}")
				.addHeader("Content-type", "application/json")
				.secure
		).either

		//handle the response
		response() match {
			case Right(res) => true
			case _ => false
		}
	}
}