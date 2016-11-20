package facades

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

case class FacadeModule() extends AbstractModule with ScalaModule {

  protected def configure() {
    //bind[Trait].to[Implement]
  }
}
