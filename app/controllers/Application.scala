package controllers

import javax.inject.{Inject, Singleton}

import play.api.Configuration
import play.api.mvc.{Action, Controller}

@Singleton
class Application @Inject()(config: Configuration) extends Controller {

  def index = Action { request =>
    Ok(views.html.index("Hello World !"))
  }

}
