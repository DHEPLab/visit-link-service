# Healthy Future Backend

## 本地开发

```
$ cp src/main/resources/application-local.sample.yml src/main/resources/application-local.yml 
```

在 src/main/resources/application-local.yml 文件配置 MySQL 和 Aliyun OSS

提前建好数据库 healthy --character_set_server=utf8mb4 --collation-server=utf8mb4_bin

```
$ SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

默认用户:
* Username: admin
* Password: admin123

## 外部依赖

* Aliyun OSS，用来存储课程的图片和视频

## 部署脚本

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
