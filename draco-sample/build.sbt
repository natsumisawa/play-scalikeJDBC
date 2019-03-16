name := """draco-sample"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test,
  "org.scalikejdbc" %% "scalikejdbc"       % "3.3.2",
  "com.h2database"  %  "h2"                % "1.4.197",
  "ch.qos.logback"  %  "logback-classic"   % "1.2.3",
  "org.scalikejdbc" %% "scalikejdbc-config" % "3.0.0", // Connectionのセットアップを簡単にする
  "org.scalikejdbc"   %% "scalikejdbc-test" % "3.2.+"   % "test",
  "org.scalatest"     %% "scalatest"        % "3.0.+"   % "test",
  "org.specs2"        %% "specs2-core"      % "3.8.9"   % "test"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
