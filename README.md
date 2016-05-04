# robopatrol-server

[![Build Status](https://travis-ci.org/robopatrol/robopatrol-server.svg?branch=master)](https://travis-ci.org/robopatrol/robopatrol-server) [![codecov.io](https://codecov.io/github/robopatrol/robopatrol-server/coverage.svg?branch=master)](https://codecov.io/github/robopatrol/robopatrol-server?branch=master)

## Develop

### Run the Application

`gradle run`

`gradle run --debug`

The REST server should run on:  http://localhost:9998/

### IDE

I recommend Intellij. Choose "Import from Gradle" and everything should work just fine.

## Release

### Docker

Releases are published as Docker containers: [Robopatrol on Docker Hub](https://hub.docker.com/u/robopatrol/)

[Get Docker](https://docs.docker.com/engine/installation/), then build and run a Docker container:

```shell
docker-compose build
docker-compose up
```

(The server runs on port 9998. Use `docker-machine ip` to get the IP address.)

### Publish a Release

Build and upload a container image:

```shell
docker build -t robopatrol/server .
docker push robopatrol/server
```
