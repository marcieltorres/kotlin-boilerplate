.PHONY: build test run clean help db-up db-down db-logs lint

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

lint: ## Check code with detekt
	./gradlew detekt

help:
	@grep -E '^[a-zA-Z_-]+:.*##' Makefile | awk 'BEGIN {FS = ":.*##"}; {printf "  %-12s %s\n", $$1, $$2}'

db-up: generate-default-env-file
	docker compose up db -d

db-down:
	docker compose down

db-logs:
	docker compose logs -f db

generate-default-env-file:
	@if [ ! -f .env ]; then cp .env.template .env; fi;