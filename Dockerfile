FROM bitnami/wildfly:30.0.0 as base

USER root

RUN apt-get update && \
      apt-get -y install sudo

WORKDIR /bitnami/wildfly

RUN sudo apt-get install -y wget && \
    mkdir /opt/bitnami/wildfly/modules/system/layers/base/com/postgres && \
    mkdir /opt/bitnami/wildfly/modules/system/layers/base/com/postgres/main && \
    sudo wget -P /opt/bitnami/wildfly/modules/system/layers/base/com/postgres/main \
    https://repo1.maven.org/maven2/org/postgresql/postgresql/42.7.0/postgresql-42.7.0.jar

# Download gradle
RUN sudo apt-get install unzip
RUN mkdir /usr/gradle
RUN sudo wget -P /usr/gradle https://services.gradle.org/distributions/gradle-8.3-bin.zip
RUN unzip /usr/gradle/gradle-8.3-bin.zip -d /usr/gradle
ENV GRADLE_HOME=/usr/gradle/gradle-8.3
ENV PATH=$PATH:$GRADLE_HOME/bin

COPY ./wildfly-conf/module.xml /opt/bitnami/wildfly/modules/system/layers/base/com/postgres/main/module.xml
COPY ./wildfly-conf/standalone.xml /opt/bitnami/wildfly/standalone/configuration/standalone.xml

FROM base as build

ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

COPY ./build.gradle.kts ./settings.gradle.kts $APP_HOME
COPY ./com.issue.tracker.domain/build.gradle.kts $APP_HOME/com.issue.tracker.domain/build.gradle.kts
COPY ./com.issue.tracker.api/build.gradle.kts $APP_HOME/com.issue.tracker.api/build.gradle.kts
COPY ./com.issue.tracker.api.persistence/build.gradle.kts $APP_HOME/com.issue.tracker.api.persistence/build.gradle.kts
COPY ./com.issue.tracker.infra/build.gradle.kts $APP_HOME/com.issue.tracker.infra/build.gradle.kts
COPY ./com.issue.tracker.infra.persistence/build.gradle.kts $APP_HOME/com.issue.tracker.infra.persistence/build.gradle.kts
COPY ./com.issue.tracker.infra.web/build.gradle.kts $APP_HOME/com.issue.tracker.infra.web/build.gradle.kts
COPY ./build.gradle.kts $APP_HOME/build.gradle.kts
COPY ./settings.gradle.kts $APP_HOME/settings.gradle.kts

RUN gradle clean build --no-daemon > /dev/null 2>&1 || true

COPY ./com.issue.tracker.domain $APP_HOME/com.issue.tracker.domain
COPY ./com.issue.tracker.api $APP_HOME/com.issue.tracker.api
COPY ./com.issue.tracker.api.persistence $APP_HOME/com.issue.tracker.api.persistence
COPY ./com.issue.tracker.infra $APP_HOME/com.issue.tracker.infra
COPY ./com.issue.tracker.infra.persistence $APP_HOME/com.issue.tracker.infra.persistence
COPY ./com.issue.tracker.infra.web $APP_HOME/com.issue.tracker.infra.web
COPY ./gradle/libs.versions.toml $APP_HOME/gradle/libs.versions.toml
COPY ./build.properties $APP_HOME/build.properties
COPY ./src $APP_HOME/src

RUN gradle build

RUN cp ./build/libs/issue-tracker.ear /opt/bitnami/wildfly/standalone/deployments/issue-tracker.ear

CMD [ "/opt/bitnami/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "-bconsole", "0.0.0.0" ]
