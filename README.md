# robopatrol-server

[![Build Status](https://travis-ci.org/robopatrol/robopatrol-server.svg?branch=master)](https://travis-ci.org/robopatrol/robopatrol-server) [![codecov.io](https://codecov.io/github/robopatrol/robopatrol-server/coverage.svg?branch=master)](https://codecov.io/github/robopatrol/robopatrol-server?branch=master)

Main project documentation: **[Project Wiki](https://github.com/robopatrol/robopatrol/wiki)**

## Develop

### Run the Application

`gradle run`

`gradle run --debug`

The REST server should run on:  http://localhost:9998/

### Ressources

The REST server delivers ressources, represented as JSON Strings. Each can be modified via the methods GET, POST, PUT and DELETE. Each item has a generated id and can be modified via /ressource/{id}. Ressources available, with a short example of their JSON-attributes are:

**/maps**
```
  {
      "name": "Livingroom",
      "filename": "xy/map.xy"
  }
```
**/pictures**
```
  {
      "name": "Grumpy cat",
      "image": "..............................."
  }
```
**/route**
```
  {
      "name": "Point #1",
      "x": "234",
      "y": "25"
  }
```
**/schedule**
```
  {
      "name": "Hourly Patrol",
      "description": "This should keep the cats at bay!",
      "cron": "0 12 * * *"
  }
```

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
