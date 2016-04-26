# robopatrol-server

[![Build Status](https://travis-ci.org/robopatrol/robopatrol-server.svg?branch=master)](https://travis-ci.org/robopatrol/robopatrol-server) [![codecov.io](https://codecov.io/github/robopatrol/robopatrol-server/coverage.svg?branch=master)](https://codecov.io/github/robopatrol/robopatrol-server?branch=master)

## Run the application for testing/development

`gradle run`
`gradle run --debug`

Server should run on:  http://localhost:9998/

## IDE

I recommend Intellij. Choose "Import from Gradle" and everything should work just fine.

## Docker

If you haven't already, install Docker: https://docs.docker.com/engine/installation/

Build and Run:

```shell
docker-compose build
docker-compose up
```

The server runs on port 9998. Use `docker-machine ip` to get the IP address.
