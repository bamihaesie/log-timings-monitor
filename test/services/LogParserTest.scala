package services

import org.scalatest.FunSuite
import model.LogEntry
import org.scalatest.matchers.ShouldMatchers
import java.util
import io.BufferedSource
import java.io.{File, FileInputStream}

class LogParserTest extends FunSuite with ShouldMatchers {

    test("extract timings") {
      val source: BufferedSource = new BufferedSource(new FileInputStream(new File("test/resources/tomcat-ocp.log.txt")))
      val timings: util.ArrayList[LogEntry] = LogParser.extractTimings(source)
      timings.size should equal (191)
    }

}
