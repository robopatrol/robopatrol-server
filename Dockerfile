FROM java:8

ADD . robopatrol-server
WORKDIR robopatrol-server

VOLUME /robopatrol-server/store
RUN ./gradlew installDist

CMD ./build/install/robopatrol-server/bin/robopatrol-server
EXPOSE 9998
