#!/bin/bash
#
# SPDX-License-Identifier: Apache-2.0

# Exit on first error,  all commands.
#set -ev
#set -o pipefail

# Where am I?
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

export FABRIC_CFG_PATH="${DIR}/config"

sudo rm -rf ${DIR}/wallet/*
sudo rm -rf ${DIR}/gateway/*
sudo rm -rf ${DIR}/chaincode/FileChain/build

cd "${DIR}/test-network/"

./network.sh down

# remove any stopped containers
docker rm -f $(docker ps -aq)

