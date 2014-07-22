Nark PagerDuty Plugin
=====================
The Nark PagerDuty Plugin sends Nark alerts to PagerDuty. It can trigger incidents and resolve them according to the Nark system. To use the PagerDuty plugin, do the following:

* Include the Nark PagerDuty dependency in Nark's Build.scala.

```
libraryDependencies ++= Seq(
    "com.lucidchart" %% "nark-pagerduty-plugin" % "1.0"
)
```

* Include the plugin the application.conf

```
plugins.names = [
    "PagerDuty"
]
plugins.PagerDuty.class = "com.lucidchart.open.nark.pagerduty.PagerDuty"
```

* Create the plugin configuration file. This defaults to `conf/nark_pagerduty.conf` but can also be set by setting an environment variable called `NARK_PAGERDUTY_CONF` to the location of the configuration file. The configuration file should contain the following values:

    * `pagerduty.api.key` - id of the pagerduty service created for nark
    * `pagerduty.tags` - only alerts with these tags will be sent to PagerDuty. They should be comma separated.
    * `pagerduty.fallbackEmails` - emails to contact if some error occurs in notifying PagerDuty. Should be comma separated.

```
pagerduty.api.key = {id}
pagerduty.tags = ops,mysql
pagerduty.fallbackEmails = ops@example.com,steve@example.com
```