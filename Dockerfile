FROM java:8
# Can't use 8-alpine because the start script requires bash.

# Run `gradle build` before running this Docker build! 
ADD build/distributions/robopatrol-server.tar .
WORKDIR robopatrol-server
VOLUME /robopatrol-server/store
CMD ./bin/robopatrol-server
EXPOSE 9998
