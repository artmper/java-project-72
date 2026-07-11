.DEFAULT_GOAL := build-run

setup:
	./gradlew wrapper --gradle-version 8.7

clean:
	./gradlew clean

build:
	./gradlew clean build

install:
	./gradlew clean install

run-dist:
	@./build/install/app/bin/app

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

lint:
	./gradlew checkstyleMain

update-deps:
	./gradlew dependencyUpdates -Drevision=release


build-run: build install run-dist

.PHONY: build