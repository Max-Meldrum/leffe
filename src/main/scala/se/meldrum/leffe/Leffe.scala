package se.meldrum.leffe

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

import scala.io.Source
import scala.util.{Failure, Success, Try}



case class Stock(name: String,
                 url :String,
                 price: Option[String] = None,
                 lastUpdated: Option[String] = None) {

  def analyze(): Try[Stock] = Try {
    val browser = JsoupBrowser()
    val priceRegex = """(\d+,\d+)""".r
    val timeRegex = """(\d+:\d+:\d+)""".r
    val lastPrice = browser.get(url) >?> element(".lastPrice")
    lastPrice match {
      case Some(ele) =>
        val lPrice = (priceRegex findFirstIn ele.innerHtml).map(_.toString)
        val updated = (timeRegex findFirstIn ele.innerHtml).map(_.toString)
        this.copy(price = lPrice, lastUpdated = updated)
      case None =>
        this
    }
  }
}

object Leffe extends App {
  val stocks = fetchStocks("stocks")
  stocks match {
    case Success(seq) =>
      val analyzed = seq.map(_.analyze())
      println("Name\tPrice\tLast Updated")
      analyzed.foreach {a =>
        a match {
          case Success(st) => displayStock(st)
          case Failure(e) => println("Something went wrong")
        }
      }

    case Failure(e) => println(e)
  }

  private def displayStock(s: Stock): Unit = {
    val p = s.price.getOrElse("0")
    val time = s.lastUpdated.getOrElse("Unknown")
    println(Console.MAGENTA + s"${s.name}\t" + Console.BLUE + "\t" +  p + Console.CYAN + "\t" +  time)
  }

  private def fetchStocks(file: String): Try[Seq[Stock]] = Try {
    Source.fromFile(file)
      .getLines()
      .filter(line => !line.startsWith("#"))
      .map(_.split(",") match {case Array(a,b) => Stock(a,b)})
      .toList
  }
}
