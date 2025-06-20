SHELL := /bin/bash

init-env:
	# Make script executable
	chmod +x ./docker/add_etc_hosts_entry.sh
	# Append database hostname in `hosts` file
	sudo ./docker/add_etc_hosts_entry.sh
	# Source .bash_profile so that sdk function is visible && Init SDKMAN configuration
	source ~/.bash_profile && sdk env install

build:
	# -B enables mvnd verbose logging
	mvnd -B clean package -Dsmartbuilder.profiling=true

generate-code:
	// Clean
	rm -rf src/main/java/com/petromirdzhunev/controller/*
	rm -rf src/main/java/com/petromirdzhunev/repository/jooq/*
	mvnd -B clean generate-sources -P generate-code
	# Putting back the files to git
	git add src/main/java/com/petromirdzhunev/controller/*
	git add src/main/java/com/petromirdzhunev/repository/jooq/*

clean-database:
	docker-compose --file ./docker/docker-compose.yaml down
	docker-compose --file ./docker/docker-compose.yaml up -d --remove-orphans