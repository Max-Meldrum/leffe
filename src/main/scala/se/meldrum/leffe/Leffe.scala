/*
 * Copyright 2017 Max Meldrum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.meldrum.leffe

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.io.Source
import scala.util.{Failure, Success, Try}



case class Stock(name: String,
                 url :String,
                 price: Option[String] = None,
                 lastUpdated: Option[String] = None) {

  def analyze(): Future[Try[Stock]] = Future {
    Try {
      val browser = JsoupBrowser()
      val priceRegex = """(\d+,\d+)""".r
      val timeRegex = """(\d+:\d+:\d+)""".r
      val lastPrice = browser.get(url) >?> element(".lastPrice")
      lastPrice match {
        case Some(ele) =>
          val lPrice = (priceRegex findFirstIn ele.innerHtml).map(_.toString)
          val updated = (timeRegex findFirstIn ele.innerHtml).map(_.toString)
          copy(price = lPrice, lastUpdated = updated)
        case None =>
          this
      }
    }
  }

  def displayStock(): Unit = {
    val p = price.getOrElse("0")
    val time = lastUpdated.getOrElse("Unknown")
    println(String.format("%5s %5s %5s", Console.GREEN + s"$name", Console.BLUE + p, Console.CYAN + time))
  }
}

object Leffe extends App {
  val file = "stocks"
  println(Console.BOLD + "Fetching info from -> " + file)
  val stocks = fetchStocks(file)

  stocks match {
    case Success(seq) =>
      val analyzed = Future.sequence(seq.map(_.analyze()))
      analyzed.onComplete {
        case Success(st) =>
          println(String.format("%5s %5s %5s", Console.YELLOW + "[" +Console.GREEN +
            "Name", Console.BLUE + "Price", Console.CYAN + "Last updated" + Console.YELLOW + "]"))
          // Original stock object will always exist.. hence why _.get
          st.foreach(_.get.displayStock())
        case Failure(e) =>
          println(Console.RED + "Something went wrong..")
          println(e.getMessage)
      }
      Await.ready(analyzed, 20.seconds)
    case Failure(e) => println(e)
  }

  private def fetchStocks(file: String): Try[Seq[Stock]] = Try {
    Source.fromFile(file)
      .getLines()
      .filter(line => !line.startsWith("#") && !line.isEmpty)
      .map(_.split(",") match {case Array(a,b) => Stock(a,b)})
      .toList
  }
}
