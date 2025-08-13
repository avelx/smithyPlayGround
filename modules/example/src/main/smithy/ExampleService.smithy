namespace smithy4s.example

use alloy#simpleRestJson

@simpleRestJson
service CountryService {
  version: "1.0.0",
  operations: [GetCountry]
}

@http(method: "GET", uri: "/country/{code}", code: 200)
operation GetCountry {
  input: Code,
  output: Country
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