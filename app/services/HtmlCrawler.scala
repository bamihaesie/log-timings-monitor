package services

import scala.xml.Node
import scala.collection.mutable.ListBuffer

object HtmlCrawler {

  def extractLogFileNames(baseUrl: String) : List[String] = {

    val parserFactory = new org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
    val parser = parserFactory.newSAXParser()
    val source = new org.xml.sax.InputSource(baseUrl)
    val adapter = new scala.xml.parsing.NoBindingFactoryAdapter
    val root: Node = adapter.loadXML(source, parser)

    val lst : ListBuffer[String] = new ListBuffer[String]

    val table = root.nonEmptyChildren(1).nonEmptyChildren(3)
    for (i <- 0 to table.nonEmptyChildren.size - 1) {
      val tr = table.nonEmptyChildren(i)
      if (tr.nonEmptyChildren.size == 4) {
        val fileName = tr.nonEmptyChildren(1).nonEmptyChildren(0).attribute("href").get.toString()
        if (fileName.startsWith("tomcat")) {
          lst.append(fileName.toString())
        }
      }
    }

    lst.toList
  }

}
