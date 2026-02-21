.PHONY: build test run clean help

# loading and exporting all env vars from .env file automatically
ifneq (,$(wildcard ./.env))
    include .env
    export
endif

build: generate-default-env-file
	./gradlew build

test:
	./gradlew test

run:
	./gradlew bootRun

clean:
	./gradlew clean

help:
	@grep -E '^[a-zA-Z_-]+:.*##' Makefile | awk 'BEGIN {FS = ":.*##"}; {printf "  %-12s %s\n", $$1, $$2}'

generate-default-env-file:
	@if [ ! -f .env ]; then cp .env.template .env; fi;