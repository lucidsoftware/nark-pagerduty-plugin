package com.lucidchart.open.nark.pagerduty

import scala.io.Source

object Configuration {

	private val conf = readConf()

	/**
	 * Get a value from the db configuration by its key
	 * @param key the key to look for
	 * @return the value associated with the key
	 */
	def get(key: String): Option[String] = conf.get(key)

	/**
	 * Read in the db configuration file as a Map of key value pairs
	 * @return the key value pairs
	 */
	private def readConf(): Map[String, String] = {
		val file = sys.env.get("NARK_PAGERDUTY_CONF").getOrElse("conf/nark_pagerduty.conf")
		val source = Source.fromFile(file)
		val lines = source.getLines.toList
		source.close()

		lines.foldLeft(Map[String, String]()) { (result, line) =>
			val pairs = line.split("=")
			result + (pairs(0).trim -> pairs(1).trim)
		}
	}

}