package services

import cats.effect.{IO, Resource}
import cats.implicits._
import doobie.implicits._
import doobie.util.transactor.Transactor.Aux
import doobie.{ConnectionIO, Transactor}
import org.http4s.HttpRoutes
import smithy4s.example
import smithy4s.example._

import cats.effect._
import cats.implicits._
import org.http4s.implicits._
import org.http4s.ember.server._
import org.http4s._
import com.comcast.ip4s._
import smithy4s.http4s.SimpleRestJsonBuilder

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

  def find(code: String): ConnectionIO[Option[Country]] =
    sql"select code, name from country where code = $code".query[Country].option

  def getCountry(code: String): IO[Country] = {
    transactor.use { conn =>
      for {
        country <- find(code).transact(conn)
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
