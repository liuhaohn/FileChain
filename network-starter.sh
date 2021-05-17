#!/bin/bash
#
# SPDX-License-Identifier: Apache-2.0

# Exit on first error, print all commands.
set -ev
set -o pipefail

# Where am I? 这里定位的是脚本存放的目录，直接用 `pwd` 在执行脚本时不再脚本目录会出问题
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

export FABRIC_CFG_PATH="${DIR}/config"

pushd "${DIR}/test-network/"

./network.sh down
./network.sh up -ca
./network.sh createChannel -c filenet

# Copy the connection profiles so they are in the correct organizations.
cp "${DIR}/test-network/organizations/peerOrganizations/org1.example.com/connection-org1.yaml" "${DIR}/gateway/"
cp "${DIR}/test-network/organizations/peerOrganizations/org2.example.com/connection-org2.yaml" "${DIR}/gateway/"
cp ${DIR}/test-network/organizations/peerOrganizations/org1.example.com/*.id ${DIR}/wallet
cp ${DIR}/test-network/organizations/peerOrganizations/org2.example.com/*.id ${DIR}/wallet

./network.sh deployCC -l java -c filenet -n fncc
popd

./network-addorg3.sh