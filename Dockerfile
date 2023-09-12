FROM mobiledevops/android-sdk-image:33.0.2

USER root
WORKDIR /builder
COPY . /builder
RUN /builder/gradlew build
CMD /builder/gradlew run