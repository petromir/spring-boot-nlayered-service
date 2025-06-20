#!/bin/bash
# Simple script to add a Docker compose service entries to /etc/hosts
# This script requires sudo privileges to modify /etc/hosts
# Usage: sudo ./add_etc_hosts_entry.sh

set -e

# Define constants
HOSTS_FILE="/etc/hosts"
DOCKER_HOSTS_MARKER="# Docker compose services"

# Check if script is run with sudo
if [ "$(id -u)" -ne 0 ]; then
    echo "Error: This script requires sudo privileges to modify $HOSTS_FILE"
    echo "Please run with: sudo $0"
    exit 1
fi

# Append the following lines to /etc/hosts file
if ! grep -q "# Something" "$HOSTS_FILE"; then
    echo "Appending '127.0.0.1 db' to $HOSTS_FILE file"
    echo "# Docker compose services" >> "$HOSTS_FILE"
    echo "127.0.0.1 db" >> "$HOSTS_FILE"
    if [ $? -ne 0 ]; then
        echo "Error: Failed to update hosts file"
        exit 1
    fi
    echo "Successfully appended '127.0.0.1 db' to $HOSTS_FILE"
else
  echo "Docker compose services already added to $HOSTS_FILE file"
fi

exit 0