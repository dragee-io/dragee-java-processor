# Dragee-java-processor

## Installation

Dependencies are provided through the use of a bom dependency.

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.dragee</groupId>
            <artifactId>dragee-bom</artifactId>
            <version>${dragee.version}</version>
            <scope>import</scope>
            <type>pom</type>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## How to process your project

This dependency is mandatory to process objects that are assignable to Dragee.

```xml
<dependencies>
    <dependency>
        <groupId>io.dragee</groupId>
        <artifactId>annotation-processor</artifactId>
        <scope>provided</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>${maven.compiler.version}</version>
            <configuration>
                <annotationProcessorPaths>
                    <path>
                        <groupId>io.dragee</groupId>
                        <artifactId>annotation-processor</artifactId>
                        <version>${dragee.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## Core Annotations

Core annotations of Dragee format. It is **already provided** by the `annotation-processor` dependency.

```xml
<dependency>
    <groupId>io.dragee</groupId>
    <artifactId>core-annotations</artifactId>
    <scope>provided</scope>
</dependency>
```

## DDD Annotations

If you want to use the DDD tactical pattern annotations, you must add the following dependency:

```xml
<dependency>
    <groupId>io.dragee</groupId>
    <artifactId>ddd-annotations</artifactId>
    <scope>provided</scope>
</dependency>
```

### Clean Architecture Annotations

If you want to use the Clean Architecture annotations, you must add the following dependency:

```xml
<dependency>
    <groupId>io.dragee</groupId>
    <artifactId>clean-annotations</artifactId>
    <scope>provided</scope>
</dependency>
```