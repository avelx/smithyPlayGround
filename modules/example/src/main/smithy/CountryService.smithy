namespace smithy4s.country

use alloy#simpleRestJson

@simpleRestJson
service CountryService {
  version: "1.0.0",
  operations: [CountryOps]
}

@http(method: "GET", uri: "/{name}", code: 200)
operation CountryOps {
  input: Name,
  output: Country
}

structure Name {
  @httpLabel
  @required
  name: String
}

structure Country {
  @required
  code: String

  @required
  name: String
}