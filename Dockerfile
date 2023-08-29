FROM mobiledevops/android-sdk-image:33.0.2

WORKDIR /builder
COPY . /builder
CMD /builder/gradlew build
RUN /builder/gradlew run