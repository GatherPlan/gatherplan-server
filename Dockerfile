# OpenJDK 이미지를 베이스로 사용
FROM openjdk:17-oracle

# 애플리케이션 JAR 파일 이름 설정
ARG JAR_FILE=build/libs/GatherPlan-0.0.1-SNAPSHOT.jar

# 컨테이너 내의 작업 디렉터리 설정
WORKDIR /app

# JAR 파일을 컨테이너 내로 복사
COPY ${JAR_FILE} app.jar

# 포트 설정 (애플리케이션이 사용하는 포트)
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
