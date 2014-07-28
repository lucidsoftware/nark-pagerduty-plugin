package com.lucidchart.open.nark.pagerduty

import com.typesafe.config.{Config, ConfigFactory}
import java.io.File
import scala.collection.JavaConversions._

object Configuration {

	private val conf = readConf()

	/**
	 * Get a value from the configuration by its key
	 * @param key the key to look for
	 * @return the value associated with the key
	 */
	def get(key: String): String = conf.getString(key)

	/**
	 * Get a list of strings from the configuration by its key
	 * @param key the key to look for
	 * @return the value associated with the key
	 */
	def getList(key: String): List[String] = conf.getStringList(key).toList

	/**
	 * Read in the configuration file as a Map of key value pairs
	 * @return the key value pairs
	 */
	private def readConf(): Config = {
		val file = sys.env.get("NARK_PAGERDUTY_CONF").getOrElse("conf/nark_pagerduty.conf")
		ConfigFactory.parseFile(new File(file))
	}

}