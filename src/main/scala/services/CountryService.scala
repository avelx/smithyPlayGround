package services

import cats.effect.{IO, Resource}
import cats.implicits._
import doobie.implicits._
import org.http4s.HttpRoutes
import smithy4s.example._

import smithy4s.http4s.SimpleRestJsonBuilder

object CountryImpl extends CountryService[IO] with CountryRepo {

  def getCountries: IO[Countries] = {
    transactor.use { conn =>
      for {
        countries <- getAll.transact(conn)
      } yield Countries( countries )// or throw Error
    }
  }

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
