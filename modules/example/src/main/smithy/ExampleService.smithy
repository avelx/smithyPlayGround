namespace smithy4s.example

use alloy#simpleRestJson

list CountryList {
    member: Country
}

@simpleRestJson
service CountryService {
  version: "1.0.0",
  operations: [GetCountry, GetCountries]
}

@http(method: "GET", uri: "/country/{code}", code: 200)
operation GetCountry {
    input: Code,
    output: Country
}

@http(method: "GET", uri: "/country/all", code: 200)
operation GetCountries {
    output: Countries
}

structure Code {
    @httpLabel
    @required
    code: String,
}

structure Country{
  @required
  code: String

  @required
  name: String
}

structure Countries{
    @required
    countries: CountryList
}