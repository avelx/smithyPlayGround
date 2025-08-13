package services

import cats.effect.{IO, Resource}
import doobie.util.transactor.Transactor.Aux
import smithy4s.example.Country
import doobie.implicits._
import doobie.{ConnectionIO, Transactor}

trait CountryRepo {
  val transactor: Resource[IO, Aux[IO, Unit]] = {
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

  def getAll: ConnectionIO[List[Country]] =
    sql"select code, name from country".query[Country].to[List]

}
