FROM mobiledevops/android-sdk-image:33.0.2

USER root
WORKDIR $HOME/builder
COPY . $HOME/builder
RUN ~/builder/gradlew build
CMD ~/builder/gradlew run