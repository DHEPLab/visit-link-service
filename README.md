# Healthy Future Backend

## Local Development

```
$ cp src/main/resources/application-local.sample.yml src/main/resources/application-local.yml 
```

Configure MySQL connection info in the src/main/resources/application-local.yml file.

Create the database healthy --character_set_server=utf8mb4 --collation-server=utf8mb4_bin

```
$ SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

Default user credentials:
* Username: admin
* Password: admin123

## External Dependencies

* AWS S3, used for storing course images and videos.

## Deployment Scripts

```
$ ansible/build.sh
$ ansible/package.sh
$ DEPLOY_GROUP=prod ansible/deploy.sh
```

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/gradle-plugin/reference/html/)
* [Checkstyle](https://checkstyle.sourceforge.io/)
    * [google-java-format](https://github.com/google/google-java-format)
    * [google-java-format IntelliJ plugin](https://github.com/google/google-java-format#intellij-android-studio-and-other-jetbrains-ides)
* [Spotbugs](https://spotbugs.github.io/)
