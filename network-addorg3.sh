#!/bin/bash
# 启动org3，将之加入channel filenet
pushd ./test-network/addOrg3 || exit
./addOrg3.sh up -c filenet -ca
popd || exit

sleep 10

# 在org3中添加fncc
pushd ./test-network || exit
# $CHANNEL_NAME $CC_SRC_LANGUAGE $VERSION $CLI_DELAY $MAX_RETRY $VERBOSE $CONTRACT_NAME
./scripts/org3-scripts/deployCCOrg3.sh filenet java 1 3 5 false fncc
# 生成连接配置和钱包身份
./scripts/org3-scripts/ccp-generate.sh
./scripts/org3-scripts/id-generate.sh
popd || exit

cp ./test-network/organizations/peerOrganizations/org3.example.com/connection-org3.yaml ./gateway/
cp ./test-network/organizations/peerOrganizations/org3.example.com/*.id ./wallet