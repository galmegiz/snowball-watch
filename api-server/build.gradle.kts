plugins {
    java
    `java-library`
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("java")
}

dependencies {
    implementation(project(":commons"))
    implementation(project(":domain"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.data:spring-data-commons")

    // Springdoc
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    // DataSource
    implementation("mysql:mysql-connector-java:8.0.33")
}

repositories {
    // detekt 정적 분석 이후 report로 출력하기 위해서는 아래 repository 주소 추가함
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

tasks.bootRun {
    System.getProperties().forEach {
        systemProperty(it.key.toString(), it.value)
    }
    systemProperty("spring.profiles.active", "local")
}
