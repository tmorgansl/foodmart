# `Foodmart`

[![Build Status](https://travis-ci.com/tmorgansl/foodmart.svg?branch=master)](https://travis-ci.com/tmorgansl/scraper)
[![License](https://img.shields.io/github/license/tmorgansl/foodmart.svg)]()

A demo command line application for querying a dummy mysql database instance

## Running the application

The easiest way to run the application is to run the application using the maven wrapper and the exec plugin:-

```
./mvnw exec:java -Dexec.args='--mysql.password=<PASSWORD> --mysql.username=<USERNAME>' -q
```

Replace `<USERNAME>` and `<PASSWORD>` with the supplied credentials

## Tests

A basic set of unit tests can be ran using the maven wrapper:-

```
./mvnw test
```

Future work on the project would involve adding integration tests using a mock database.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details