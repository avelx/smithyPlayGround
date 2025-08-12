import smithy4s.country._
import cats.effect._
import cats.implicits._
import org.http4s.implicits._
import org.http4s.ember.server._
import org.http4s._
import com.comcast.ip4s._
import doobie.implicits.toSqlInterpolator
import doobie.{ConnectionIO, Transactor}
import doobie.util.transactor.Transactor.Aux
import smithy4s.http4s.SimpleRestJsonBuilder
import doobie.implicits._
import services.Routes


object Main extends IOApp.Simple {

  val run = Routes.all
    .flatMap { routes =>
      EmberServerBuilder
        .default[IO]
        .withPort(port"9000")
        .withHost(host"localhost")
        .withHttpApp(routes.orNotFound)
        .build
    }
    .use(_ => IO.never)

}