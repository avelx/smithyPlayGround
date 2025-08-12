package services

import cats.effect.{IO, Resource}
import doobie.util.transactor.Transactor.Aux
import org.http4s.HttpRoutes
import smithy4s.country.{Country, CountryService}
import smithy4s.http4s.SimpleRestJsonBuilder
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

object CountryImpl extends CountryService[IO] {

  private val transactor: Resource[IO, Aux[IO, Unit]] = {
    Resource.pure(
      Transactor.fromDriverManager[IO] (
        driver = "org.postgresql.Driver",
        url = "jdbc:postgresql://localhost:5432/geo",
        user = "postgres",
        password = "avel_pass",
        logHandler = None
      )
    )
  }

  def find(name: String): ConnectionIO[Option[Country]] =
    sql"select code, name from country where name = $name".query[Country].option

  def countryOps(name: String): IO[Country] = {
    transactor.use { conn =>
      for {
        country <- find(name).transact(conn)
      } yield country.get // or throw Error
    }
  }
}

object Routes {
  private val example: Resource[IO, HttpRoutes[IO]] =
    SimpleRestJsonBuilder.routes(CountryImpl).resource

  private val docs: HttpRoutes[IO] =
    smithy4s.http4s.swagger.docs[IO](CountryService)

  val all: Resource[IO, HttpRoutes[IO]] = example.map(_ <+> docs)
}
