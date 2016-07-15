package controllers

import javax.inject._

import akka.actor.ActorSystem
import play.api.mvc._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}

@Singleton
class StaticHtmlController @Inject() extends Controller {

  def serveBlankPage = Action{
    Ok(views.html.blankPage())
  }

  def serveBootstrapElements = Action{
    Ok(views.html.bootstrapElements())
  }
  def serveBootstrapGrid = Action{
    Ok(views.html.bootstrapGrid())
  }
  def serveCharts = Action{
    Ok(views.html.charts())
  }
  def serveForms = Action{
    Ok(views.html.forms())
  }
  def serveIndex = Action{
    Ok(views.html.index("TestMessage"))
  }

  def serveTables = Action{
    Ok(views.html.tables())
  }
}
