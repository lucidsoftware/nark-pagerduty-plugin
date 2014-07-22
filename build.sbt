import sbt.Resolver

name := "Nark PagerDuty Plugin"

organization := "com.lucidchart"

version := "1.0"

scalaVersion := "2.10.4"

exportJars := true

exportJars in Test := false

autoScalaLibrary := true

retrieveManaged := true

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.1",
  "com.lucidchart" %% "nark-plugin" % "1.0"
)

resolvers ++= List(
  Resolver.file("local ivy repository", file(System.getenv("HOME") + "/.ivy2/local/"))(Resolver.ivyStylePatterns),
  Resolver.file("opt ivy repository", file("/opt/play-2.1.1/repository/local/"))(Resolver.ivyStylePatterns)
)

pomExtra := (
  <url>https://github.com/lucidsoftware/nark-pagerduty-plugin</url>
  <licenses>
    <license>
      <name>Apache License</name>
      <url>http://www.apache.org/licenses/</url>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:lucidsoftware/nark-pagerduty-plugin.git</url>
    <connection>scm:git:git@github.com:lucidsoftware/nark-pagerduty-plugin.git</connection>
  </scm>
  <developers>
    <developer>
      <id>msiebert</id>
      <name>Mark Siebert</name>
    </developer>
  </developers>
)

pomIncludeRepository := { _ => false }

useGpg := true

pgpReadOnly := false

publishMavenStyle := true

credentials += Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", System.getenv("SONATYPE_USERNAME"), System.getenv("SONATYPE_PASSWORD"))

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
