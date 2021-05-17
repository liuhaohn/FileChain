

# 1. 环境准备

重要的链接：

- [官方入门文档](https://hyperledger-fabric.readthedocs.io/zh_CN/latest/getting_started.html) 

- Hyperledger Fabric 智能合约（链码） SDK

  > - [Go 合约 API](https://github.com/hyperledger/fabric-contract-api-go).
  > - [Node.js 合约 API](https://github.com/hyperledger/fabric-chaincode-node) and [Node.js 合约 API 文档](https://fabric-shim.github.io/).
  > - [Java 合约 API](https://github.com/hyperledger/fabric-chaincode-java) and [Java 合约 API 文档](https://hyperledger.github.io/fabric-chaincode-java/).

- Hyperledge Fabric 应用程序 SDK

  > - [Node.js SDK](https://github.com/hyperledger/fabric-sdk-node) and [Node.js SDK 文档](https://hyperledger.github.io/fabric-sdk-node/).
  > - [Java SDK](https://github.com/hyperledger/fabric-gateway-java) and [Java SDK 文档](https://hyperledger.github.io/fabric-gateway-java/)

## 1.1. 安装Docker和Docker Compose

- [菜鸟教程](https://www.runoob.com/docker/ubuntu-docker-install.html)

- ```bash
  sudo curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun
  ```

- ```bash
  sudo apt-get install docker-compose
  ```
  
- 修改或添加/etc/docker/daemon.json以加速docker镜像拉取速度

  ```json
  {
    "registry-mirrors": ["https://kuamavit.mirror.aliyuncs.com", "https://docker.mirrors.ustc.edu.cn"]
  }
  ```

  ```bash
  sudo service docker restart
  ```


## 1.2. Go语言环境

- [菜鸟教程](https://www.runoob.com/go/go-environment.html)

- [安装包](https://golang.google.cn/dl/)

- go安装在`/opt` 目录下，在 `~` 目录下创建本地的`go`目录，用于下载第三方包和第三方编译好的文件（cryptogen等）
  
  ```bash
  # 在.profile中
  export GOROOT=/opt/go
  export PATH=$PATH:$GOROOT/bin
  export GOPATH=~/go
  export PATH=$PATH:$GOPATH/bin
export GOPROXY=https://goproxy.cn	# go的第三方包下载代理（默认的被墙）
  # export GOPROXY=https://mirrors.aliyun.com/goproxy/
  export GOSUMDB=off					# 不检查ckecksum，由于使用第三方代理可能ckecksum通不过
  # 应用环境
  source .profile
  ```
  

## 1.3. node.js安装

- ```bash
  sudo apt-get install nodejs
  sudo apt-get install npm
  ```

## 1.4. java环境

- ```bash
  # sudo apt-get install openjdk-14-jdk # 不需要安装高版本，安装了要设置JAVA_HOME
  sudo apt-get install openjdk-8-jdk
  ```

- ```bash
  # 在.profile中
  export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
  ```
  
- 配置Maven仓库，在`~/.m2/setting.xml`

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
  	<mirrors>
  		<mirror>
          		<id>alimaven</id>
  	        	<name>aliyun maven</name>
  	        	<url>https://maven.aliyun.com/repository/public</url>
  	        	<mirrorOf>central</mirrorOf>
  		</mirror>
  	</mirrors>
  </settings>
  ```
  
- Gradle全局仓库，在`~/.gradle/init.gradle`

  ```groovy
  gradle.projectsLoaded {
      rootProject.allprojects {
          buildscript {
              repositories {
                  def JCENTER_URL = 'https://maven.aliyun.com/repository/jcenter'
                  def GOOGLE_URL = 'https://maven.aliyun.com/repository/google'
                  def NEXUS_URL = 'http://maven.aliyun.com/nexus/content/repositories/jcenter'
                  all { ArtifactRepository repo ->
                      if (repo instanceof MavenArtifactRepository) {
                          def url = repo.url.toString()
                          if (url.startsWith('https://jcenter.bintray.com/')) {
                              project.logger.lifecycle "Repository ${repo.url} replaced by $JCENTER_URL."
                              println("buildscript ${repo.url} replaced by $JCENTER_URL.")
                              remove repo
                          }
                          else if (url.startsWith('https://dl.google.com/dl/android/maven2/')) {
                              project.logger.lifecycle "Repository ${repo.url} replaced by $GOOGLE_URL."
                              println("buildscript ${repo.url} replaced by $GOOGLE_URL.")
                              remove repo
                          }
                          else if (url.startsWith('https://repo1.maven.org/maven2')) {
                              project.logger.lifecycle "Repository ${repo.url} replaced by $REPOSITORY_URL."
                              println("buildscript ${repo.url} replaced by $REPOSITORY_URL.")
                              remove repo
                          }
                      }
                  }
                  jcenter {
                      url JCENTER_URL
                  }
                  google {
                      url GOOGLE_URL
                  }
                  maven {
                      url NEXUS_URL
                  }
              }
          }
          repositories {
              def JCENTER_URL = 'https://maven.aliyun.com/repository/jcenter'
              def GOOGLE_URL = 'https://maven.aliyun.com/repository/google'
              def NEXUS_URL = 'http://maven.aliyun.com/nexus/content/repositories/jcenter'
              all { ArtifactRepository repo ->
                  if (repo instanceof MavenArtifactRepository) {
                      def url = repo.url.toString()
                      if (url.startsWith('https://jcenter.bintray.com/')) {
                          project.logger.lifecycle "Repository ${repo.url} replaced by $JCENTER_URL."
                          println("buildscript ${repo.url} replaced by $JCENTER_URL.")
                          remove repo
                      }
                      else if (url.startsWith('https://dl.google.com/dl/android/maven2/')) {
                          project.logger.lifecycle "Repository ${repo.url} replaced by $GOOGLE_URL."
                          println("buildscript ${repo.url} replaced by $GOOGLE_URL.")
                          remove repo
                      }
                      else if (url.startsWith('https://repo1.maven.org/maven2')) {
                          project.logger.lifecycle "Repository ${repo.url} replaced by $REPOSITORY_URL."
                          println("buildscript ${repo.url} replaced by $REPOSITORY_URL.")
                          remove repo
                      }
                  }
              }
              jcenter {
                  url JCENTER_URL
              }
              google {
                  url GOOGLE_URL
              }
              maven {
                  url NEXUS_URL
              }
          }
      }
  }
  ```

  

## 1.5. Linux配置

5. sudo 找不到命令可以进行如下修改：

   ```bash
   sudo vim /etc/sudoers
   # 找到Defaults env_reset, 将其改为Defaults !env_reset
   vim  /home/[username]/.bashrc
   # 追加alias sudo='sudo env PATH=$PATH'
   ```
   
6. sudo命令免密码：

   打开`/etc/sudoers`

   ```bash
   #增加一行
   your_user_name     ALL=(ALL:ALL) NOPASSWD:ALL
   #修改
   %sudo   ALL=(ALL:ALL) ALL
   为
   %sudo   ALL=(ALL:ALL) NOPASSWD:ALL
   ```
   
7. apt换清华源

   Ubuntu 的软件源配置文件是 `/etc/apt/sources.list`

   ```bash
   # 默认注释了源码镜像以提高 apt update 速度，如有需要可自行取消注释
   deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ focal main restricted universe multiverse
   # deb-src https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ focal main restricted universe multiverse
   deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ focal-updates main restricted universe multiverse
   # deb-src https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ focal-updates main restricted universe multiverse
   deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ focal-backports main restricted universe multiverse
   # deb-src https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ focal-backports main restricted universe multiverse
   deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ focal-security main restricted universe multiverse
   # deb-src https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ focal-security main restricted universe multiverse
   ```

   然后：

   ```bash
   sudo apt-get update
   ```

## 1.6. fabric的模板和docker容器

- 通常使用fabric-samples中的test-network作为模板来配置网络。

```bash
# 下载脚本，运行可以下载fabric-samples、bin、config以及拉取一些docker镜像
# wget https://bit.ly/2ysbOFE -O bootstarp.sh
curl -sSL https://bit.ly/2ysbOFE | bash -s -- 2.0.0 1.4.4 0.4.18
```

- 网络被墙无法运行以下方式解决：

  1. 配置/etc/hosts以访问github

     ```ba
     # GitHub Start
     52.74.223.119     github.com
     192.30.253.119    gist.github.com
     54.169.195.247    api.github.com
     185.199.111.153   assets-cdn.github.com
     151.101.76.133    raw.githubusercontent.com
     151.101.76.133    gist.githubusercontent.com
     151.101.76.133    cloud.githubusercontent.com
     151.101.76.133    camo.githubusercontent.com
     151.101.76.133    avatars0.githubusercontent.com
     151.101.76.133    avatars1.githubusercontent.com
     151.101.76.133    avatars2.githubusercontent.com
     151.101.76.133    avatars3.githubusercontent.com
     151.101.76.133    avatars4.githubusercontent.com
     151.101.76.133    avatars5.githubusercontent.com
     151.101.76.133    avatars6.githubusercontent.com
     151.101.76.133    avatars7.githubusercontent.com
     151.101.76.133    avatars8.githubusercontent.com
     # GitHub End
     ```

  2. 修改bootstarp.sh脚本以加速github的下载

     - 将`https://github.com/`改成`http://download.fastgit.org/`
     - 以下为bootstrap.sh可以直接复制

     ```bash
     #!/bin/bash
     #
     # Copyright IBM Corp. All Rights Reserved.
     #
     # SPDX-License-Identifier: Apache-2.0
     #
     
     # if version not passed in, default to latest released version
     VERSION=2.1.1
     # if ca version not passed in, default to latest released version
     CA_VERSION=1.4.7
     # current version of thirdparty images (couchdb, kafka and zookeeper) released
     THIRDPARTY_IMAGE_VERSION=0.4.20
     ARCH=$(echo "$(uname -s|tr '[:upper:]' '[:lower:]'|sed 's/mingw64_nt.*/windows/')-$(uname -m | sed 's/x86_64/amd64/g')")
     MARCH=$(uname -m)
     
     printHelp() {
         echo "Usage: bootstrap.sh [version [ca_version [thirdparty_version]]] [options]"
         echo
         echo "options:"
         echo "-h : this help"
         echo "-d : bypass docker image download"
         echo "-s : bypass fabric-samples repo clone"
         echo "-b : bypass download of platform-specific binaries"
         echo
         echo "e.g. bootstrap.sh 2.1.1 1.4.7 0.4.20 -s"
         echo "would download docker images and binaries for Fabric v2.1.1 and Fabric CA v1.4.7"
     }
     
     # dockerPull() pulls docker images from fabric and chaincode repositories
     # note, if a docker image doesn't exist for a requested release, it will simply
     # be skipped, since this script doesn't terminate upon errors.
     
     dockerPull() {
         #three_digit_image_tag is passed in, e.g. "1.4.7"
         three_digit_image_tag=$1
         shift
         #two_digit_image_tag is derived, e.g. "1.4", especially useful as a local tag for two digit references to most recent baseos, ccenv, javaenv, nodeenv patch releases
         two_digit_image_tag=$(echo "$three_digit_image_tag" | cut -d'.' -f1,2)
         while [[ $# -gt 0 ]]
         do
             image_name="$1"
             echo "====> hyperledger/fabric-$image_name:$three_digit_image_tag"
             docker pull "hyperledger/fabric-$image_name:$three_digit_image_tag"
             docker tag "hyperledger/fabric-$image_name:$three_digit_image_tag" "hyperledger/fabric-$image_name"
             docker tag "hyperledger/fabric-$image_name:$three_digit_image_tag" "hyperledger/fabric-$image_name:$two_digit_image_tag"
             shift
         done
     }
     
     cloneSamplesRepo() {
         # clone (if needed) hyperledger/fabric-samples and checkout corresponding
         # version to the binaries and docker images to be downloaded
         if [ -d first-network ]; then
             # if we are in the fabric-samples repo, checkout corresponding version
             echo "===> Checking out v${VERSION} of hyperledger/fabric-samples"
             git checkout v${VERSION}
         elif [ -d fabric-samples ]; then
             # if fabric-samples repo already cloned and in current directory,
             # cd fabric-samples and checkout corresponding version
             echo "===> Checking out v${VERSION} of hyperledger/fabric-samples"
             cd fabric-samples && git checkout v${VERSION}
         else
             echo "===> Cloning hyperledger/fabric-samples repo and checkout v${VERSION}"
             git clone -b master http://download.fastgit.org/hyperledger/fabric-samples.git && cd fabric-samples && git checkout v${VERSION}
         fi
     }
     
     # This will download the .tar.gz
     download() {
         local BINARY_FILE=$1
         local URL=$2
         echo "===> Downloading: " "${URL}"
         curl -L --retry 5 --retry-delay 3 "${URL}" | tar xz || rc=$?
         if [ -n "$rc" ]; then
             echo "==> There was an error downloading the binary file."
             return 22
         else
             echo "==> Done."
         fi
     }
     
     pullBinaries() {
         echo "===> Downloading version ${FABRIC_TAG} platform specific fabric binaries"
         download "${BINARY_FILE}" "http://download.fastgit.org/hyperledger/fabric/releases/download/v${VERSION}/${BINARY_FILE}"
         if [ $? -eq 22 ]; then
             echo
             echo "------> ${FABRIC_TAG} platform specific fabric binary is not available to download <----"
             echo
             exit
         fi
     
         echo "===> Downloading version ${CA_TAG} platform specific fabric-ca-client binary"
         download "${CA_BINARY_FILE}" "http://download.fastgit.org/hyperledger/fabric-ca/releases/download/v${CA_VERSION}/${CA_BINARY_FILE}"
         if [ $? -eq 22 ]; then
             echo
             echo "------> ${CA_TAG} fabric-ca-client binary is not available to download  (Available from 1.1.0-rc1) <----"
             echo
             exit
         fi
     }
     
     pullDockerImages() {
         command -v docker >& /dev/null
         NODOCKER=$?
         if [ "${NODOCKER}" == 0 ]; then
             FABRIC_IMAGES=(peer orderer ccenv tools)
             case "$VERSION" in
             1.*)
                 FABRIC_IMAGES+=(javaenv)
                 shift
                 ;;
             2.*)
                 FABRIC_IMAGES+=(nodeenv baseos javaenv)
                 shift
                 ;;
             esac
             echo "FABRIC_IMAGES:" "${FABRIC_IMAGES[@]}"
             echo "===> Pulling fabric Images"
             dockerPull "${FABRIC_TAG}" "${FABRIC_IMAGES[@]}"
             echo "===> Pulling fabric ca Image"
             CA_IMAGE=(ca)
             dockerPull "${CA_TAG}" "${CA_IMAGE[@]}"
             echo "===> Pulling thirdparty docker images"
             THIRDPARTY_IMAGES=(zookeeper kafka couchdb)
             dockerPull "${THIRDPARTY_TAG}" "${THIRDPARTY_IMAGES[@]}"
             echo
             echo "===> List out hyperledger docker images"
             docker images | grep hyperledger
         else
             echo "========================================================="
             echo "Docker not installed, bypassing download of Fabric images"
             echo "========================================================="
         fi
     }
     
     DOCKER=true
     SAMPLES=true
     BINARIES=true
     
     # Parse commandline args pull out
     # version and/or ca-version strings first
     if [ -n "$1" ] && [ "${1:0:1}" != "-" ]; then
         VERSION=$1;shift
         if [ -n "$1" ]  && [ "${1:0:1}" != "-" ]; then
             CA_VERSION=$1;shift
             if [ -n  "$1" ] && [ "${1:0:1}" != "-" ]; then
                 THIRDPARTY_IMAGE_VERSION=$1;shift
             fi
         fi
     fi
     
     # prior to 1.2.0 architecture was determined by uname -m
     if [[ $VERSION =~ ^1\.[0-1]\.* ]]; then
         export FABRIC_TAG=${MARCH}-${VERSION}
         export CA_TAG=${MARCH}-${CA_VERSION}
         export THIRDPARTY_TAG=${MARCH}-${THIRDPARTY_IMAGE_VERSION}
     else
         # starting with 1.2.0, multi-arch images will be default
         : "${CA_TAG:="$CA_VERSION"}"
         : "${FABRIC_TAG:="$VERSION"}"
         : "${THIRDPARTY_TAG:="$THIRDPARTY_IMAGE_VERSION"}"
     fi
     
     BINARY_FILE=hyperledger-fabric-${ARCH}-${VERSION}.tar.gz
     CA_BINARY_FILE=hyperledger-fabric-ca-${ARCH}-${CA_VERSION}.tar.gz
     
     # then parse opts
     while getopts "h?dsb" opt; do
         case "$opt" in
             h|\?)
                 printHelp
                 exit 0
                 ;;
             d)  DOCKER=false
                 ;;
             s)  SAMPLES=false
                 ;;
             b)  BINARIES=false
                 ;;
         esac
     done
     
     if [ "$SAMPLES" == "true" ]; then
         echo
         echo "Clone hyperledger/fabric-samples repo"
         echo
         cloneSamplesRepo
     fi
     if [ "$BINARIES" == "true" ]; then
         echo
         echo "Pull Hyperledger Fabric binaries"
         echo
         pullBinaries
     fi
     if [ "$DOCKER" == "true" ]; then
         echo
         echo "Pull Hyperledger Fabric docker images"
         echo
         pullDockerImages
     fi
     ```

  3. 做好准备工作后（有bootstarp.sh，改了可用的链接），就可以执行bootstrap.sh

     ```bash
     sudo ./bootstrap.sh 2.0.0 1.4.4 0.4.18 -s
     ```
  

# 2. 使用test-network和fabcar学习搭建网络

## 2.1. 启动test-network网络

移动`bootstrap`下载的`config`到`fabric-samples`目录下，`bin`中命令到 `~/go/bin` 下

启动网络：

```bash
hao@hao-Lenovo:~/IdeaProject/fabric-samples/test-network$ sudo ./network.sh up
Starting nodes with CLI timeout of '5' tries and CLI delay of '3' seconds and using datsudo ./network.sh upabase 'leveldb' 

LOCAL_VERSION=2.0.0
DOCKER_IMAGE_VERSION=2.0.0
Creating network "net_test" with the default driver
Creating volume "net_orderer.example.com" with default driver
Creating volume "net_peer0.org1.example.com" with default driver
Creating volume "net_peer0.org2.example.com" with default driver
Creating peer0.org1.example.com ... done
Creating peer0.org2.example.com ... done
Creating orderer.example.com    ... done

hao@hao-Lenovo:~/IdeaProject/fabric-samples/test-network$ sudo docker ps -a
CONTAINER ID        IMAGE                               COMMAND             CREATED             STATUS              PORTS                              NAMES
04f637a49c23        hyperledger/fabric-peer:latest      "peer node start"   57 seconds ago      Up 52 seconds       7051/tcp, 0.0.0.0:9051->9051/tcp   peer0.org2.example.com
fac3da525e2a        hyperledger/fabric-orderer:latest   "orderer"           57 seconds ago      Up 52 seconds       0.0.0.0:7050->7050/tcp             orderer.example.com
fdcd51a93823        hyperledger/fabric-peer:latest      "peer node start"   58 seconds ago      Up 52 seconds       0.0.0.0:7051->7051/tcp             peer0.org1.example.com
```

脚本原理如下：

### 2.1.1. <span id="s211">创建证书和密钥</span>

启动代码创建了order和2个org的证书和密钥文件，使用的是：

```bash
organizations/cryptogen/
├── crypto-config-orderer.yaml
├── crypto-config-org1.yaml
└── crypto-config-org2.yaml
```

生成时加了`-ca`选项，则会使用 `registerEnroll.sh` 来生成证书

```bash
organizations/fabric-ca/
├── ordererOrg
│   └── fabric-ca-server-config.yaml
├── org1
│   └── fabric-ca-server-config.yaml
├── org2
│   └── fabric-ca-server-config.yaml
└── registerEnroll.sh
```

生成文件位于是：

```bash
organizations/ordererOrganizations/
└── example.com
    ├── ca
    ├── msp
    ├── orderers
    ├── tlsca
    └── users
organizations/peerOrganizations/
├── org1.example.com
│   ├── ca
│   ├── connection-org1.json
│   ├── connection-org1.yaml
│   ├── msp
│   ├── peers
│   ├── tlsca
│   └── users
└── org2.example.com
    ├── ca
    ├── connection-org2.json
    ├── connection-org2.yaml
    ├── msp
    ├── peers
    ├── tlsca
    └── users

```

- 使用cryptogen工具创建证书和密钥(`the certificates and keys`)的脚本：

```bash
function createOrgs() {
  # Create crypto material using cryptogen
  if [ "$CRYPTO" == "cryptogen" ]; then
	cryptogen generate --config=./organizations/cryptogen/crypto-config-org1.yaml --output="organizations"

    cryptogen generate --config=./organizations/cryptogen/crypto-config-orderer.yaml --output="organizations"
  fi
    echo "Generate CCP files for Org1 and Org2，用于nodejs登录时使用"
    ./organizations/ccp-generate.sh
}
```

- 使用ca服务器创建证书和密钥的脚本（最终网站注册还是要用ca服务器）：

```bash
  # Create crypto material using Fabric CAs
  if [ "$CRYPTO" == "Certificate Authorities" ]; then
	# 启动使用docker-compose-ca.yaml启动ca客户端，ca客户端可以实时注册用户
    IMAGE_TAG=${CA_IMAGETAG} docker-compose -f $COMPOSE_FILE_CA up -d 2>&1
	# 导入新脚本
    . organizations/fabric-ca/registerEnroll.sh
	# 注册机构
	createOrg1
    createOrg2
    createOrderer
  fi
  
  fabric-ca-client enroll -u https://admin:adminpw@localhost:7054 --caname ca-org1 --tls.certfiles ${PWD}/organizations/fabric-ca/org1/tls-cert.pem

	echo "Register peer0"
	fabric-ca-client register --caname ca-org1 --id.name peer0 --id.secret peer0pw --id.type peer --tls.certfiles ${PWD}/organizations/fabric-ca/org1/tls-cert.pem

  echo "Register user"
  fabric-ca-client register --caname ca-org1 --id.name user1 --id.secret user1pw --id.type client --tls.certfiles ${PWD}/organizations/fabric-ca/org1/tls-cert.pem

  echo "Register the org admin"
  fabric-ca-client register --caname ca-org1 --id.name org1admin --id.secret org1adminpw --id.type admin --tls.certfiles ${PWD}/organizations/fabric-ca/org1/tls-cert.pem

  echo "## Generate the peer0 msp"
	fabric-ca-client enroll -u https://peer0:peer0pw@localhost:7054 --caname ca-org1 -M ${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/msp --csr.hosts peer0.org1.example.com --tls.certfiles ${PWD}/organizations/fabric-ca/org1/tls-cert.pem

  echo "## Generate the peer0-tls certificates"
  fabric-ca-client enroll -u https://peer0:peer0pw@localhost:7054 --caname ca-org1 -M ${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls --enrollment.profile tls --csr.hosts peer0.org1.example.com --csr.hosts localhost --tls.certfiles ${PWD}/organizations/fabric-ca/org1/tls-cert.pem

  echo "## Generate the user msp"
	fabric-ca-client enroll -u https://user1:user1pw@localhost:7054 --caname ca-org1 -M ${PWD}/organizations/peerOrganizations/org1.example.com/users/User1@org1.example.com/msp -

  echo "## Generate the org admin msp"
	fabric-ca-client enroll -u https://org1admin:org1adminpw@localhost:7054 --caname ca-org1 -M ${PWD}/organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp --tls.certfiles ${PWD}/organizations/fabric-ca/org1/tls-cert.pem
```

### 2.1.2. 创建系统链的创始块

使用了 `configtx/configtx.yaml` 下的 `TwoOrgsOrdererGenesis` 来创建 `system-genesis-block\genesis.block` 其创建了可以使用的联盟Consortiums，在后面创建通道时可以使用此联盟。（后期加入新的联盟成员参考`addOrg3` 目录）

```yaml
Profiles:
    TwoOrgsOrdererGenesis:
        <<: *ChannelDefaults
        Orderer:
            <<: *OrdererDefaults
            Organizations:
                - *OrdererOrg
            Capabilities:
                <<: *OrdererCapabilities
        Consortiums:
            SampleConsortium:
                Organizations:
                    - *Org1
                    - *Org2
                    
    TwoOrgsChannel:
        Consortium: SampleConsortium
        <<: *ChannelDefaults
        Application:
            <<: *ApplicationDefaults
            Organizations:
                - *Org1
                - *Org2
            Capabilities:
                <<: *ApplicationCapabilities

```

```bash
system-genesis-block/
└── genesis.block
```

创建的主要代码如下：

```bash
function createConsortium() {

  configtxgen -profile TwoOrgsOrdererGenesis -channelID system-channel -outputBlock ./system-genesis-block/genesis.block

}
```

### 2.1.3. docker-compose启动网络

使用了 `docker/docker-compose-test-net.yaml` 启动了一个order容器，两个org容器，该文件主要如下：

特别注意要使容器在同一个网络networks要配置正确。

```yaml
version: '2'

volumes:
  orderer.example.com:
  peer0.org1.example.com:
  peer0.org2.example.com:

networks:
  test:

services:
  orderer.example.com:
    ...
    networks:
      - test

  peer0.org1.example.com:
    container_name: peer0.org1.example.com
    image: hyperledger/fabric-peer:$IMAGE_TAG
    environment:
    	- CORE_VM_DOCKER_HOSTCONFIG_NETWORKMODE=${COMPOSE_PROJECT_NAME}_test
    	...
    volumes:
        ...
    working_dir: /opt/gopath/src/github.com/hyperledger/fabric/peer
    command: peer node start
    ports:
      - 7051:7051
    networks:
      - test

  peer0.org2.example.com:
  	...
```

启动网络的主要脚本如下：

```bash
function networkUp() {

  checkPrereqs  # 检查../config目录是否存在，存在表明执行了bootstrap.sh脚本，并检查cryptogen等命令是否可以执行
  # generate artifacts if they don't exist
  if [ ! -d "organizations/peerOrganizations" ]; then
    createOrgs
    createConsortium
  fi

  COMPOSE_FILES="-f ${COMPOSE_FILE_BASE}"

  if [ "${DATABASE}" == "couchdb" ]; then
    COMPOSE_FILES="${COMPOSE_FILES} -f ${COMPOSE_FILE_COUCH}"
  fi

  IMAGE_TAG=$IMAGETAG docker-compose ${COMPOSE_FILES} up -d 2>&1

  docker ps -a
  if [ $? -ne 0 ]; then
    echo "ERROR !!!! Unable to start network"
    exit 1
  fi
}
```

## 2.2. 创建channel

> 该部分需要注意文件的使用

```bash
sudo ./network.sh createChannel -c fabcarchannel
```

创建通道会使用 `configtx.yaml` 文件中的 `TwoOrgsChannel` 配置项，用于生成 `.tx` 文件，该文件用于生成 `.block` 文件，该文件传给orgs即各组织加入通道：

```yaml
Profiles:
    TwoOrgsOrdererGenesis:
        <<: *ChannelDefaults
        Orderer:
            <<: *OrdererDefaults
            Organizations:
                - *OrdererOrg
            Capabilities:
                <<: *OrdererCapabilities
        Consortiums:
            SampleConsortium:
                Organizations:
                    - *Org1
                    - *Org2
    TwoOrgsChannel:
        Consortium: SampleConsortium
        <<: *ChannelDefaults
        Application:
            <<: *ApplicationDefaults
            Organizations:
                - *Org1
                - *Org2
            Capabilities:
                <<: *ApplicationCapabilities
```

生成文件(最后 `.block` 文件即该通道的)：

```bash
channel-artifacts/
├── fabcarchannel.block	# 由fabcarchannel.tx生成，用于Orgs加入通道
├── fabcarchannel.tx	# 由TwoOrgsChannel生成，用于创建fabcarchannel.block
├── Org1MSPanchors.tx	# 由TwoOrgsChannel生成，用于Org1更新Anchor节点
└── Org2MSPanchors.tx
```

创建通道会使用到config/core.yaml文件，该文件对通道进行默认配置。

登录到不同的节点只要更改一些环境变量，使用的是scripts/envVar.sh：

```bash
setGlobals() {
  local USING_ORG=""
  if [ -z "$OVERRIDE_ORG" ]; then
    USING_ORG=$1
  else
    USING_ORG="${OVERRIDE_ORG}"
  fi
  echo "Using organization ${USING_ORG}"
  if [ $USING_ORG -eq 1 ]; then
    export CORE_PEER_LOCALMSPID="Org1MSP"
    export CORE_PEER_TLS_ROOTCERT_FILE=$PEER0_ORG1_CA
    export CORE_PEER_MSPCONFIGPATH=${PWD}/organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp
    export CORE_PEER_ADDRESS=localhost:7051
  elif [ $USING_ORG -eq 2 ]; then
    export CORE_PEER_LOCALMSPID="Org2MSP"
    export CORE_PEER_TLS_ROOTCERT_FILE=$PEER0_ORG2_CA
    export CORE_PEER_MSPCONFIGPATH=${PWD}/organizations/peerOrganizations/org2.example.com/users/Admin@org2.example.com/msp
    export CORE_PEER_ADDRESS=localhost:9051

  elif [ $USING_ORG -eq 3 ]; then
    export CORE_PEER_LOCALMSPID="Org3MSP"
    export CORE_PEER_TLS_ROOTCERT_FILE=$PEER0_ORG3_CA
    export CORE_PEER_MSPCONFIGPATH=${PWD}/organizations/peerOrganizations/org3.example.com/users/Admin@org3.example.com/msp
    export CORE_PEER_ADDRESS=localhost:11051
  else
    echo "================== ERROR !!! ORG Unknown =================="
  fi

  if [ "$VERBOSE" == "true" ]; then
    env | grep CORE
  fi
}
```

创建channel流程，使用/scripts/createChannel.sh脚本：

### 2.2.1. 生成fabcarchannel.tx

```bash
### Generating channel create transaction 'fabcarchannel.tx' ###
+ configtxgen -profile TwoOrgsChannel -outputCreateChannelTx ./channel-artifacts/fabcarchannel.tx -channelID fabcarchannel
```

```bash
function createChannel() {
 # 调用scripts中的脚本创建channel
 scripts/createChannel.sh $CHANNEL_NAME $CLI_DELAY $MAX_RETRY $VERBOSE
}

createChannelTx() {
	configtxgen -profile TwoOrgsChannel -outputCreateChannelTx ./channel-artifacts/${CHANNEL_NAME}.tx -channelID $CHANNEL_NAME
}
```

### 2.2.2. 生成OrgMSPanchors.tx

```bash
### Generating anchor peer update transactions ###
#######    Generating anchor peer update transaction for Org1MSP  ##########
+ configtxgen -profile TwoOrgsChannel -outputAnchorPeersUpdate ./channel-artifacts/Org1MSPanchors.tx -channelID fabcarchannel -asOrg Org1MSP
#######    Generating anchor peer update transaction for Org2MSP  ##########
+ configtxgen -profile TwoOrgsChannel -outputAnchorPeersUpdate ./channel-artifacts/Org2MSPanchors.tx -channelID fabcarchannel -asOrg Org2MSP
```

```bash
createAncorPeerTx() {
	for orgmsp in Org1MSP Org2MSP; do
		configtxgen -profile TwoOrgsChannel -outputAnchorPeersUpdate ./channel-artifacts/${orgmsp}anchors.tx -channelID $CHANNEL_NAME -asOrg ${orgmsp}
	done
}
```

### 2.2.3. 由fabcarchannel.tx创建channel，生成fabcarchannel.block

```bash
Creating channel fabcarchannel
Using organization 1
+ peer channel create -o localhost:7050 -c fabcarchannel --ordererTLSHostnameOverride orderer.example.com -f ./channel-artifacts/fabcarchannel.tx --outputBlock ./channel-artifacts/fabcarchannel.block --tls --cafile /home/hao/IdeaProject/fabric-samples/test-network/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem
===================== Channel 'fabcarchannel' created ===================== 
```

```bash
createChannel() {
	setGlobals 1
	while [ $rc -ne 0 -a $COUNTER -lt $MAX_RETRY ] ; do
		peer channel create -o localhost:7050 -c $CHANNEL_NAME --ordererTLSHostnameOverride orderer.example.com -f ./channel-artifacts/${CHANNEL_NAME}.tx --outputBlock ./channel-artifacts/${CHANNEL_NAME}.block --tls --cafile $ORDERER_CA >&log.txt
	done
}
```

### 2.2.4. 通过fabcarchannel.block各orgs加入channel

```bash
Join Org1 peers to the channel...
Using organization 1
+ peer channel join -b ./channel-artifacts/fabcarchannel.block

Join Org2 peers to the channel...
Using organization 2
+ peer channel join -b ./channel-artifacts/fabcarchannel.block
```

```bash
joinChannel() {
	ORG=$1
	setGlobals $ORG
	while [ $rc -ne 0 -a $COUNTER -lt $MAX_RETRY ] ; do
    peer channel join -b ./channel-artifacts/$CHANNEL_NAME.block >&log.txt
	done
}
echo "Join Org1 peers to the channel..."
joinChannel 1
echo "Join Org2 peers to the channel..."
joinChannel 2
```

### 2.2.5. 各Orgs更新锚节点

```bash
Updating anchor peers for org1...
Using organization 1
+ peer channel update -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com -c fabcarchannel -f ./channel-artifacts/Org1MSPanchors.tx --tls --cafile /home/hao/IdeaProject/fabric-samples/test-network/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem
===================== Anchor peers updated for org 'Org1MSP' on channel 'fabcarchannel' ===================== 

Updating anchor peers for org2...
Using organization 2
+ peer channel update -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com -c fabcarchannel -f ./channel-artifacts/Org2MSPanchors.tx --tls --cafile /home/hao/IdeaProject/fabric-samples/test-network/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem
===================== Anchor peers updated for org 'Org2MSP' on channel 'fabcarchannel' ===================== 
```

```bash
updateAnchorPeers() {
  ORG=$1
  setGlobals $ORG
	## 循环用于尝试retry
	while [ $rc -ne 0 -a $COUNTER -lt $MAX_RETRY ] ; do
		peer channel update -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com -c $CHANNEL_NAME -f ./channel-artifacts/${CORE_PEER_LOCALMSPID}anchors.tx --tls --cafile $ORDERER_CA >&log.txt
	done
}
echo "Updating anchor peers for org1..."
updateAnchorPeers 1
echo "Updating anchor peers for org2..."
updateAnchorPeers 2
```

## 2.3. 安装、发布ChainCode

```bash
sudo ./network.sh deployCC -c fabcarchannel -l [go*|java|javascript|typescript]
```

发布ChainCode会先打包ChainCode，ChaninCode可以使用多种语言写，go是默认的，java不好编译通过，javascript比较好。

```bash
../fabcar/
├── go
│   ├── fabcar.go
│   ├── go.mod
│   └── go.sum
├── java
│   ├── pom.xml
│   ├── src
│   └── wallet
├── javascript
│   ├── enrollAdmin.js
│   ├── invoke.js
│   ├── package.json
│   ├── package-lock.json
│   ├── query.js
│   ├── registerUser.js
│   └── wallet
└── typescript
    ├── package.json
    ├── src
    ├── tsconfig.json
    ├── tslint.json
    └── wallet
```

各个组织会安装打包好ChainCode，安装好后发布到指定的channel。

流程如下：

### 2.3.1. 打包ChainCode

```bash
if [ "$CC_SRC_LANGUAGE" = "go" -o "$CC_SRC_LANGUAGE" = "golang" ] ; then
	CC_RUNTIME_LANGUAGE=golang
	CC_SRC_PATH="../chaincode/fabcar/go/"
	
	echo Vendoring Go dependencies ...
	pushd ../chaincode/fabcar/go
	GO111MODULE=on go mod vendor
	popd
	echo Finished vendoring Go dependencies
elif [ "$CC_SRC_LANGUAGE" = "javascript" ]; then
	CC_RUNTIME_LANGUAGE=node # chaincode runtime language is node.js
	CC_SRC_PATH="../chaincode/fabcar/javascript/"
elif [ "$CC_SRC_LANGUAGE" = "java" ]; then
	CC_RUNTIME_LANGUAGE=java
	CC_SRC_PATH="../chaincode/fabcar/java/build/install/fabcar"

	echo Compiling Java code ...
	pushd ../chaincode/fabcar/java
	./gradlew installDist
	popd
	echo Finished compiling Java code
elif [ "$CC_SRC_LANGUAGE" = "typescript" ]; then
	CC_RUNTIME_LANGUAGE=node # chaincode runtime language is node.js
	CC_SRC_PATH="../chaincode/fabcar/typescript/"

	echo Compiling TypeScript code into JavaScript ...
	pushd ../chaincode/fabcar/typescript
	npm install
	npm run build
	popd
	echo Finished compiling TypeScript code into JavaScript

# import utils
. scripts/envVar.sh

packageChaincode() {
  ORG=$1
  setGlobals $OR
  peer lifecycle chaincode package fabcar.tar.gz --path ${CC_SRC_PATH} --lang ${CC_RUNTIME_LANGUAGE} --label fabcar_${VERSION} >&log.txt
}
packageChaincode 1
```

```bash
Vendoring Go dependencies ...
Finished vendoring Go dependencies
Using organization 1
++ peer lifecycle chaincode package fabcar.tar.gz --path ../chaincode/fabcar/go/ --lang golang --label fabcar_1
===================== Chaincode is packaged on peer0.org1 ===================== 
```

打包ChainCode只在一个节点上打包，java需要使用gradle，go要配置好go环境，javascript要安装node.js即可，注意脚本中打包的是`../chaincode/fabcar/`中的CC

go打包的代码如下(就是`go mod endor`后打成了tar包)：

```bash
src
├── fabcar.go
├── go.mod
├── go.sum
└── vendor
    ├── github.com
    ├── golang.org
    ├── google.golang.org
    ├── gopkg.in
    └── modules.txt
```

java使用gradle打包，要注意gradle的版本和java的版本要对应，gradle版本位于`grdle/wrapper/gradle-wrapper.properties`文件中：

```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-5.6.2-all.zip
```

gradle-5.x版本要使用jdk-1.8，而gradle-6.x使用jdk-14，由于在命令行打包，所以java环境是命令行的java环境，用`java -version`查看。

有多个java环境可以配置`JAVA_HOME`变量来指定使用哪个版本的java，fabcar使用java-1.8不要配错，否则报错。**（建议不要用java写CC，坑太多了，打包安装了有时也运行不了）**

java打包后tar包目录为(与源码目录中`build/install/fabcar`的目录文件相同)：

```bash
fabcar.tar.gz/
├── code.tar.gz
│   └── src
│       ├── fabcar-1.0-SNAPSHOT.jar
│       └── lib
│           └── genson-1.5.jar
└── metadata.json
```

### 2.3.2. 各Orgs安装CC

```bash
Installing chaincode on peer0.org1...
Using organization 1
++ peer lifecycle chaincode install fabcar.tar.gz
===================== Chaincode is installed on peer0.org1 ===================== 

Install chaincode on peer0.org2...
Using organization 2
++ peer lifecycle chaincode install fabcar.tar.gz
===================== Chaincode is installed on peer0.org2 ===================== 
```

```bash
# installChaincode PEER ORG
installChaincode() {
  ORG=$1
  setGlobals $ORG
  peer lifecycle chaincode install fabcar.tar.gz >&log.txt
}
## Install chaincode on peer0.org1 and peer0.org2
echo "Installing chaincode on peer0.org1..."
installChaincode 1
echo "Install chaincode on peer0.org2..."
installChaincode 2
```

### 2.3.3. 一个Org确认CC已经被安装，获取CC ID

```bash
Using organization 1
++ peer lifecycle chaincode queryinstalled
Installed chaincodes on peer:
Package ID: fabcar_1:b930716c3c5e0df1413a3f456a576a188e101ac91faf7605865e435dc65419bc, Label: fabcar_1
===================== Query installed successful on peer0.org1 on channel ===================== 
```

```bash
# queryInstalled PEER ORG
queryInstalled() {
  ORG=$1
  setGlobals $ORG
  peer lifecycle chaincode queryinstalled >&log.txt
  PACKAGE_ID=$(sed -n "/fabcar_${VERSION}/{s/^Package ID: //; s/, Label:.*$//; p;}" log.txt)
}
## query whether the chaincode is installed
queryInstalled 1
```

这里只有Org1执行确认安装，获取了CC的ID，ID用于后面Orgs批准CC时使用

### 2.3.4. Orgs批准CC的定义，并检查各Orgs是否批准CC并可以提交到channel

```bash
Using organization 1
++ peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile /home/hao/IdeaProject/fabric-samples/test-network/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem --channelID fabcarchannel --name fabcar --version 1 --init-required --package-id fabcar_1:b930716c3c5e0df1413a3f456a576a188e101ac91faf7605865e435dc65419bc --sequence 1
===================== Chaincode definition approved on peer0.org1 on channel 'fabcarchannel' ===================== 

Using organization 1
===================== Checking the commit readiness of the chaincode definition on peer0.org1 on channel 'fabcarchannel'... ===================== 
Attempting to check the commit readiness of the chaincode definition on peer0.org1, Retry after 3 seconds.
++ peer lifecycle chaincode checkcommitreadiness --channelID fabcarchannel --name fabcar --version 1 --sequence 1 --output json --init-required
{
	"approvals": {
		"Org1MSP": true,
		"Org2MSP": false
	}
}
===================== Checking the commit readiness of the chaincode definition successful on peer0.org1 on channel 'fabcarchannel' ===================== 

Using organization 2
===================== Checking the commit readiness of the chaincode definition on peer0.org2 on channel 'fabcarchannel'... ===================== 
Attempting to check the commit readiness of the chaincode definition on peer0.org2, Retry after 3 seconds.
++ peer lifecycle chaincode checkcommitreadiness --channelID fabcarchannel --name fabcar --version 1 --sequence 1 --output json --init-required
{
	"approvals": {
		"Org1MSP": true,
		"Org2MSP": false
	}
}
===================== Checking the commit readiness of the chaincode definition successful on peer0.org2 on channel 'fabcarchannel' ===================== 

==========...org2...========
```

```bash
# approveForMyOrg VERSION PEER ORG
approveForMyOrg() {
  ORG=$1
  setGlobals $ORG
  peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile $ORDERER_CA --channelID $CHANNEL_NAME --name fabcar --version ${VERSION} --init-required --package-id ${PACKAGE_ID} --sequence ${VERSION} >&log.txt
}

# checkCommitReadiness VERSION PEER ORG
checkCommitReadiness() {
  ORG=$1
  setGlobals $ORG
	# continue to poll
  # we either get a successful response, or reach MAX RETRY
	while [ $rc -ne 0 -a $COUNTER -lt $MAX_RETRY ] ; do
    peer lifecycle chaincode checkcommitreadiness --channelID $CHANNEL_NAME --name fabcar --version ${VERSION} --sequence ${VERSION} --output json --init-required >&log.txt
	done
}
## approve the definition for org1
approveForMyOrg 1
## check whether the chaincode definition is ready to be committed
## expect org1 to have approved and org2 not to
checkCommitReadiness 1 "\"Org1MSP\": true" "\"Org2MSP\": false"
checkCommitReadiness 2 "\"Org1MSP\": true" "\"Org2MSP\": false"

## now approve also for org2
approveForMyOrg 2
## check whether the chaincode definition is ready to be committed
## expect them both to have approved
checkCommitReadiness 1 "\"Org1MSP\": true" "\"Org2MSP\": true"
checkCommitReadiness 2 "\"Org1MSP\": true" "\"Org2MSP\": true"
```

`peer lifecycle chaincode approveformyorg`用于Org接受CC的定义，定义包括包括链码名称、版本和背书策略。可以使用如 `--channel-config-policy Channel/Application/[Admins]` 指定在channel中预定义的背书策略应用于批准的CC，预定义的策略在`configtx/configtx.yaml`文件中：

```yaml
Application: &ApplicationDefaults
    # Policies defines the set of policies at this level of the config tree
    # For Application policies, their canonical path is
    #   /Channel/Application/<PolicyName>
    Policies:
        Readers:
            Type: ImplicitMeta
            Rule: "ANY Readers"
        Writers:
            Type: ImplicitMeta
            Rule: "ANY Writers"
        Admins:
            Type: ImplicitMeta
            Rule: "MAJORITY Admins"
        LifecycleEndorsement:
            Type: ImplicitMeta
            Rule: "MAJORITY Endorsement"
        Endorsement:
            Type: ImplicitMeta
            Rule: "MAJORITY Endorsement"
```

>该文件中还有组织的背书策略：
>
>```yaml
>Organizations:
>    - &OrdererOrg
>        # Policies defines the set of policies at this level of the config tree
>        # For organization policies, their canonical path is usually
>        #   /Channel/<Application|Orderer>/<OrgName>/<PolicyName>
>        Policies:
>            Readers:
>                Type: Signature
>                Rule: "OR('OrdererMSP.member')"
>            Writers:
>                Type: Signature
>                Rule: "OR('OrdererMSP.member')"
>            Admins:
>                Type: Signature
>                Rule: "OR('OrdererMSP.admin')"
>
>    - &Org1
>        # Policies defines the set of policies at this level of the config tree
>        # For organization policies, their canonical path is usually
>        #   /Channel/<Application|Orderer>/<OrgName>/<PolicyName>
>        Policies:
>            Readers:
>                Type: Signature
>                Rule: "OR('Org1MSP.admin', 'Org1MSP.peer', 'Org1MSP.client')"
>            Writers:
>                Type: Signature
>                Rule: "OR('Org1MSP.admin', 'Org1MSP.client')"
>            Admins:
>                Type: Signature
>                Rule: "OR('Org1MSP.admin')"
>            Endorsement:
>                Type: Signature
>                Rule: "OR('Org1MSP.peer')"
>	- &Org2
>        # Policies defines the set of policies at this level of the config tree
>        # For organization policies, their canonical path is usually
>        #   /Channel/<Application|Orderer>/<OrgName>/<PolicyName>
>        Policies:
>            Readers:
>                Type: Signature
>                Rule: "OR('Org2MSP.admin', 'Org2MSP.peer', 'Org2MSP.client')"
>            Writers:
>                Type: Signature
>                Rule: "OR('Org2MSP.admin', 'Org2MSP.client')"
>            Admins:
>                Type: Signature
>                Rule: "OR('Org2MSP.admin')"
>            Endorsement:
>                Type: Signature
>                Rule: "OR('Org2MSP.peer')"
>```
>
>Organizations中的背书策略和Application中的背书策略的不同是：
>
>Organizations中预定义的是一组用于Org内部的背书策略，而Application中预定义的是Orgs间的。
>
>相同点是`Endorsement`策略会被选作默认。

`peer lifecycle chaincode checkcommitreadiness`用于检查channel中有那些Orgs接受了CC的定义，可以将CC提交到channel。使用该命令可以了解是否有足够的通道成员已批准链码定义，以满足`Application/Channel`背书策略，然后才能将定义提交到通道。 

### 2.3.5. 某一个Org将提交CC到channel

```bash
Using organization 1
Using organization 2
++ peer lifecycle chaincode commit -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile /home/hao/IdeaProject/fabric-samples/test-network/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem --channelID fabcarchannel --name fabcar --peerAddresses localhost:7051 --tlsRootCertFiles /home/hao/IdeaProject/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt --peerAddresses localhost:9051 --tlsRootCertFiles /home/hao/IdeaProject/fabric-samples/test-network/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt --version 1 --sequence 1 --init-required
===================== Chaincode definition committed on channel 'fabcarchannel' ===================== 
```

```bash
# commitChaincodeDefinition VERSION PEER ORG (PEER ORG)...
commitChaincodeDefinition() {
  parsePeerConnectionParameters $@	# 给PEER_CONN_PARMS赋值
  # while 'peer chaincode' command can get the orderer endpoint from the
  # peer (if join was successful), let's supply it directly as we know
  # it using the "-o" option
  peer lifecycle chaincode commit -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile $ORDERER_CA --channelID $CHANNEL_NAME --name fabcar $PEER_CONN_PARMS --version ${VERSION} --sequence ${VERSION} --init-required >&log.txt
}
## now that we know for sure both orgs have approved, commit the definition
commitChaincodeDefinition 1 2
```

当确认足够多的组织安装批准了CC，某个组织就可以将CC提交到channel，提交时指定`-peerAddresses`这些Orgs peer地址有运行CC进行背书的权利，`-o`是order地址是channel物理存在的地方。

### 2.3.6. 各Orgs查询批准的CC是否已经被提交到channel

```bash
Using organization 1
===================== Querying chaincode definition on peer0.org1 on channel 'fabcarchannel'... ===================== 
Attempting to Query committed status on peer0.org1, Retry after 3 seconds.

++ peer lifecycle chaincode querycommitted --channelID fabcarchannel --name fabcar

Committed chaincode definition for chaincode 'fabcar' on channel 'fabcarchannel':
Version: 1, Sequence: 1, Endorsement Plugin: escc, Validation Plugin: vscc, Approvals: [Org1MSP: true, Org2MSP: true]
===================== Query chaincode definition successful on peer0.org1 on channel 'fabcarchannel' ===================== 

Using organization 2
...
```

```bash
# queryCommitted ORG
queryCommitted() {
  ORG=$1
  setGlobals $ORG
	# continue to poll
  # we either get a successful response, or reach MAX RETRY
	while [ $rc -ne 0 -a $COUNTER -lt $MAX_RETRY ] ; do
    peer lifecycle chaincode querycommitted --channelID $CHANNEL_NAME --name fabcar >&log.txt
	done
}
## query on both orgs to see that the definition committed successfully
queryCommitted 1
queryCommitted 2
```

`peer lifecycle chaincode querycommitted`各Orgs查询自己批准的CC是否已经提交到了channel

### 2.3.7. 通过channel执行Invoke传参初始化CC

```bash
Using organization 1
Using organization 2
++ peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile /home/hao/IdeaProject/fabric-samples/test-network/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C fabcarchannel -n fabcar --peerAddresses localhost:7051 --tlsRootCertFiles /home/hao/IdeaProject/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt --peerAddresses localhost:9051 --tlsRootCertFiles /home/hao/IdeaProject/fabric-samples/test-network/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt --isInit -c '{"function":"initLedger","Args":[]}'
===================== Invoke transaction successful on peer0.org1 peer0.org2 on channel 'fabcarchannel' ===================== 
```

```bash
chaincodeInvokeInit() {
  parsePeerConnectionParameters $@
  # while 'peer chaincode' command can get the orderer endpoint from the
  # peer (if join was successful), let's supply it directly as we know
  # it using the "-o" option
  peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile $ORDERER_CA -C $CHANNEL_NAME -n fabcar $PEER_CONN_PARMS --isInit -c '{"function":"initLedger","Args":[]}' >&log.txt
}
## Invoke the chaincode
chaincodeInvokeInit 1 2
```

CC的Init函数同样当成普通函数一样执行，只是加了个`-isInit`标记，go部分代码如下：

```go
// SmartContract provides functions for managing a car
type SmartContract struct {
	contractapi.Contract
}

// Car describes basic details of what makes up a car
type Car struct {
	Make   string `json:"make"`
	Model  string `json:"model"`
	Colour string `json:"colour"`
	Owner  string `json:"owner"`
}

// InitLedger adds a base set of cars to the ledger
func (s *SmartContract) InitLedger(ctx contractapi.TransactionContextInterface) error {
	cars := []Car{
		Car{Make: "Toyota", Model: "Prius", Colour: "blue", Owner: "Tomoko"},
		Car{Make: "Ford", Model: "Mustang", Colour: "red", Owner: "Brad"},
		Car{Make: "Hyundai", Model: "Tucson", Colour: "green", Owner: "Jin Soo"},
		Car{Make: "Volkswagen", Model: "Passat", Colour: "yellow", Owner: "Max"},
		Car{Make: "Tesla", Model: "S", Colour: "black", Owner: "Adriana"},
		Car{Make: "Peugeot", Model: "205", Colour: "purple", Owner: "Michel"},
		Car{Make: "Chery", Model: "S22L", Colour: "white", Owner: "Aarav"},
		Car{Make: "Fiat", Model: "Punto", Colour: "violet", Owner: "Pari"},
		Car{Make: "Tata", Model: "Nano", Colour: "indigo", Owner: "Valeria"},
		Car{Make: "Holden", Model: "Barina", Colour: "brown", Owner: "Shotaro"},
	}

	for i, car := range cars {
		carAsBytes, _ := json.Marshal(car)
		err := ctx.GetStub().PutState("CAR"+strconv.Itoa(i), carAsBytes)

		if err != nil {
			return fmt.Errorf("Failed to put to world state. %s", err.Error())
		}
	}

	return nil
}
```

### 2.3.8. 使用CC

```bash
Querying chaincode on peer0.org1...
Using organization 1
===================== Querying on peer0.org1 on channel 'fabcarchannel'... ===================== 
Attempting to Query peer0.org1, Retry after 3 seconds.
++ peer chaincode query -C fabcarchannel -n fabcar -c '{"Args":["queryAllCars"]}'

[{"Key":"CAR0","Record":{"make":"Toyota","model":"Prius","colour":"blue","owner":"Tomoko"}},{"Key":"CAR1","Record":{"make":"Ford","model":"Mustang","colour":"red","owner":"Brad"}},{"Key":"CAR2","Record":{"make":"Hyundai","model":"Tucson","colour":"green","owner":"Jin Soo"}},{"Key":"CAR3","Record":{"make":"Volkswagen","model":"Passat","colour":"yellow","owner":"Max"}},{"Key":"CAR4","Record":{"make":"Tesla","model":"S","colour":"black","owner":"Adriana"}},{"Key":"CAR5","Record":{"make":"Peugeot","model":"205","colour":"purple","owner":"Michel"}},{"Key":"CAR6","Record":{"make":"Chery","model":"S22L","colour":"white","owner":"Aarav"}},{"Key":"CAR7","Record":{"make":"Fiat","model":"Punto","colour":"violet","owner":"Pari"}},{"Key":"CAR8","Record":{"make":"Tata","model":"Nano","colour":"indigo","owner":"Valeria"}},{"Key":"CAR9","Record":{"make":"Holden","model":"Barina","colour":"brown","owner":"Shotaro"}}]
===================== Query successful on peer0.org1 on channel 'fabcarchannel' ===================== 
```

```bash
chaincodeQuery() {
  ORG=$1
  setGlobals $ORG
	# continue to poll
  # we either get a successful response, or reach MAX RETRY
	while [ $rc -ne 0 -a $COUNTER -lt $MAX_RETRY ] ; do
    peer chaincode query -C $CHANNEL_NAME -n fabcar -c '{"Args":["queryAllCars"]}' >&log.txt
	done
}
# Query chaincode on peer0.org1
echo "Querying chaincode on peer0.org1..."
chaincodeQuery 1
```

注意查询和写入的区别：

- 查询query不用指定背书节点和排序节点，不用经过背书
- 写入invork要指定背书节点和排序节点，经过背书<读写集>后传给排序节点，排序后广播

```go
// QueryAllCars returns all cars found in world state
func (s *SmartContract) QueryAllCars(ctx contractapi.TransactionContextInterface) ([]QueryResult, error) {
	startKey := ""
	endKey := ""

	resultsIterator, err := ctx.GetStub().GetStateByRange(startKey, endKey)

	if err != nil {
		return nil, err
	}
	defer resultsIterator.Close()

	results := []QueryResult{}

	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()

		if err != nil {
			return nil, err
		}

		car := new(Car)
		_ = json.Unmarshal(queryResponse.Value, car)

		queryResult := QueryResult{Key: queryResponse.Key, Record: car}
		results = append(results, queryResult)
	}

	return results, nil
}
```

注意两种参数格式是通用的，不给定`funcation`则`Args`中第一个参数即调用的函数：

- `'{"function":"CreateCar", "Args":["CAR10", "Ford", "Mustang", "yellow", "Hao"]}'`

- `'{"Args":["QueryCar", "CAR0"]}'`

函数名使用驼峰规则，首字母大小写都可以，其他区分大小写

查询语句不能调用有写入操作的方法，否则不执行，无返回

以下命令也是合法的：

```bash
$ peer chaincode query -C fabcarchannel -n fabcar -c '{"Args":["queryCar","CAR0"]}'
{"make":"Volkswagen","model":"Passat","colour":"yellow","owner":"Max"}

$ peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile /home/hao/IdeaProject/fabric-samples/test-network/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C fabcarchannel -n fabcar --peerAddresses localhost:7051 --tlsRootCertFiles /home/hao/IdeaProject/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt --peerAddresses localhost:9051 --tlsRootCertFiles /home/hao/IdeaProject/fabric-samples/test-network/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt  -c '{"function":"changeCarOwner","Args":["CAR0","Min"]}'
2020-06-23 19:56:47.821 CST [chaincodeCmd] chaincodeInvokeOrQuery -> INFO 001 Chaincode invoke successful. result: status:200 

$ peer chaincode query -C fabcarchannel -n fabcar -c '{"Args":["cueryCar","CAR0"]}'
{"make":"Toyota","model":"Prius","colour":"blue","owner":"Min"}

$ sudo peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile /home/hao/IdeaProject/fabric-samples/test-network/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C fabcarchannel -n fabcar --peerAddresses localhost:7051 --tlsRootCertFiles /home/hao/IdeaProject/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt --peerAddresses localhost:9051 --tlsRootCertFiles /home/hao/IdeaProject/fabric-samples/test-network/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt  -c '{"function":"createCar","Args":["CAR10","Ford", "Mustang", "yellow", "Hao"]}'
2020-06-23 20:02:59.490 CST [chaincodeCmd] chaincodeInvokeOrQuery -> INFO 001 Chaincode invoke successful. result: status:200 

$ peer chaincode query -C fabcarchannel -n fabcar -c '{"Args":["queryAllCars"]}'
[{"Key":"CAR0","Record":{"make":"Toyota","model":"Prius","colour":"blue","owner":"Min"}},{"Key":"CAR1","Record":{"make":"Ford","model":"Mustang","colour":"red","owner":"Brad"}},{"Key":"CAR10","Record":{"make":"Ford","model":"Mustang","colour":"yellow","owner":"Hao"}},{"Key":"CAR2","Record":{"make":"Hyundai","model":"Tucson","colour":"green","owner":"Jin Soo"}},{"Key":"CAR3","Record":{"make":"Volkswagen","model":"Passat","colour":"yellow","owner":"Max"}},{"Key":"CAR4","Record":{"make":"Tesla","model":"S","colour":"black","owner":"Adriana"}},{"Key":"CAR5","Record":{"make":"Peugeot","model":"205","colour":"purple","owner":"Michel"}},{"Key":"CAR6","Record":{"make":"Chery","model":"S22L","colour":"white","owner":"Aarav"}},{"Key":"CAR7","Record":{"make":"Fiat","model":"Punto","colour":"violet","owner":"Pari"}},{"Key":"CAR8","Record":{"make":"Tata","model":"Nano","colour":"indigo","owner":"Valeria"}},{"Key":"CAR9","Record":{"make":"Holden","model":"Barina","colour":"brown","owner":"Shotaro"}}]
```

```go
// CreateCar adds a new car to the world state with given details
func (s *SmartContract) CreateCar(ctx contractapi.TransactionContextInterface, carNumber string, make string, model string, colour string, owner string) error {
	car := Car{
		Make:   make,
		Model:  model,
		Colour: colour,
		Owner:  owner,
	}

	carAsBytes, _ := json.Marshal(car)

	return ctx.GetStub().PutState(carNumber, carAsBytes)
}

// QueryCar returns the car stored in the world state with given id
func (s *SmartContract) QueryCar(ctx contractapi.TransactionContextInterface, carNumber string) (*Car, error) {
	carAsBytes, err := ctx.GetStub().GetState(carNumber)

	if err != nil {
		return nil, fmt.Errorf("Failed to read from world state. %s", err.Error())
	}

	if carAsBytes == nil {
		return nil, fmt.Errorf("%s does not exist", carNumber)
	}

	car := new(Car)
	_ = json.Unmarshal(carAsBytes, car)

	return car, nil
}

// ChangeCarOwner updates the owner field of car with given id in world state
func (s *SmartContract) ChangeCarOwner(ctx contractapi.TransactionContextInterface, carNumber string, newOwner string) error {
	car, err := s.QueryCar(ctx, carNumber)

	if err != nil {
		return err
	}

	car.Owner = newOwner

	carAsBytes, _ := json.Marshal(car)

	return ctx.GetStub().PutState(carNumber, carAsBytes)
}
```

在本地之间执行`peer`命令要先设在一些环境变量（Org的用户msp证书），使本机可以通过Org的MSP登录到Org的peer节点：

```bash
. scripts/envVar.sh
setGlobals 1
export FABRIC_CFG_PATH=${PWD}/../config/
# 或者

# 登录经过的MSP
export CORE_PEER_LOCALMSPID="Org1MSP"
# 登录使用tls加密通信需要的证书
export CORE_PEER_TLS_ROOTCERT_FILE=${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt
# 登录msp需要的证书
export CORE_PEER_MSPCONFIGPATH=${PWD}/organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp
# 登录msp所在的peer节点的地址
export CORE_PEER_ADDRESS=localhost:7051
# core.yaml全局配置文件所在的目录
export FABRIC_CFG_PATH=${PWD}/../config/
```

## 2.4. 加入新组织

### 2.4.1. 创建证书和密钥

创建方式和[2.1.1](#s211)节中的创建的方式是一样的，使用的是`addOrg3/org3-crypto.yaml`文件，或者使用ca（先启动ca的docker容器，然后使用fabric-ca-client注册）。

```bash
############ Create Org1 Identities ######################
+ cryptogen generate --config=org3-crypto.yaml --output=../organizations

Generate CCP files for Org3
```

```bash
function generateOrg3() {

  # Create crypto material using cryptogen
  if [ "$CRYPTO" == "cryptogen" ]; then
    cryptogen generate --config=org3-crypto.yaml --output="../organizations"
  fi

  # Create crypto material using Fabric CAs
  if [ "$CRYPTO" == "Certificate Authorities" ]; then
    IMAGE_TAG=${CA_IMAGETAG} docker-compose -f $COMPOSE_FILE_CA_ORG3 up -d 2>&1
    . fabric-ca/registerEnroll.sh
    createOrg3
  fi

  ./ccp-generate.sh
}
```

ccp-generate.sh是创建connect.yaml文件的脚本，此文件用于sdk连接到组织使用，文件创建后位于`organzations/peerOraniztions/org3.example.com/connection-org3.yaml`。

### 2.4.2. 启动Org3的容器

```bash
Creating peer0.org3.example.com ... done
Creating Org3cli                ... done
```

```bash
function Org3Up () {
  # start org3 nodes
  if [ "${DATABASE}" == "couchdb" ]; then
    IMAGE_TAG=${IMAGETAG} docker-compose -f $COMPOSE_FILE_ORG3 -f $COMPOSE_FILE_COUCH_ORG3 up -d 2>&1
  else
    IMAGE_TAG=$IMAGETAG docker-compose -f $COMPOSE_FILE_ORG3 up -d 2>&1
  fi
}
```

容器主要是peer节点和一个client名为`Org3cli`，注意Org1/2没有创建client。

client容器中包含了`test-network/scripts/org3-scripts`中的脚本，以及org3的证书文件：

```bash
scripts/org3-scripts/
├── envVarCLI.sh
├── step1org3.sh
└── step2org3.sh
```

### 2.4.3. 更新channel配置

```bash
function addOrg3 () {
  # Use the CLI container to create the configuration transaction needed to add
  # Org3 to the network
  echo "####### Generate and submit config tx to add Org3 #############"
  docker exec Org3cli ./scripts/org3-scripts/step1org3.sh $CHANNEL_NAME $CLI_DELAY $CLI_TIMEOUT $VERBOSE
}
```

该脚本让`org3cli`执行了`step1org3.sh`脚本。

#### 2.4.3.1. 获取channel已有的配置，转成json文件，注入Org3配置到文件中

```bash
# fetchChannelConfig <channel_id> <output_json>
# Writes the current channel config for a given channel to a JSON file
fetchChannelConfig() {
  setOrdererGlobals
  setGlobals $ORG

  peer channel fetch config config_block.pb -o orderer.example.com:7050 --ordererTLSHostnameOverride orderer.example.com -c $CHANNEL --tls --cafile $ORDERER_CA

  configtxlator proto_decode --input config_block.pb --type common.Block | jq .data.data[0].payload.data.config >"${OUTPUT}"
}

# Fetch the config for the channel, writing it to config.json
fetchChannelConfig 1 ${CHANNEL_NAME} config.json
# Modify the configuration to append the new org
set -x
jq -s '.[0] * {"channel_group":{"groups":{"Application":{"groups": {"Org3MSP":.[1]}}}}}' config.json ./organizations/peerOrganizations/org3.example.com/org3.json > modified_config.json
set +x
```

```bash
Fetching the most recent configuration block for the channel
+ peer channel fetch config config_block.pb -o orderer.example.com:7050 --ordererTLSHostnameOverride orderer.example.com -c mychannel --tls --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem
2020-07-11 15:34:57.092 UTC [cli.common] readBlock -> INFO 002 Received block: 6
2020-07-11 15:34:57.094 UTC [channelCmd] fetch -> INFO 003 Retrieving last config block: 2
2020-07-11 15:34:57.099 UTC [cli.common] readBlock -> INFO 004 Received block: 2

Decoding config block to JSON and isolating config to config.json
+ configtxlator proto_decode --input config_block.pb --type common.Block
+ jq '.data.data[0].payload.data.config'
```

第一条命令将通道配置区块以二进制 protobuf 形式保存在 `config_block.pb` 。

日志：

```bash
2020-07-11 15:34:57.092 UTC [cli.common] readBlock -> INFO 002 Received block: 6
2020-07-11 15:34:57.094 UTC [channelCmd] fetch -> INFO 003 Retrieving last config block: 2
2020-07-11 15:34:57.099 UTC [cli.common] readBlock -> INFO 004 Received block: 2
```

==这三条日志告诉我们，系统链上block共有6+1（初始区块）块，我们最新的`mychannel` 的配置区块实际上是区块 2，后面块是对mychannel上的链码进行实例化和调用所产生。== `peer channel fetch config` 命令默认返回目标通道最新的配置区块，在这个例子里是第三个区块。 这是因为`network.sh`脚本分别在两个不同通道更新交易中为两个组织 – `Org1` 和 `Org2` –更新了锚节点。

最终，我们有如下的配置块序列：

> - block 0: genesis block
> - block 1: Org1 anchor peer update
> - block 2: Org2 anchor peer update

然后，用 `configtxlator` 工具将这个通道配置解码为 JSON 格式（以便友好地被阅读 和修改）。

然后，用`jq`工具裁剪所有的头部、元数据、创建者签名等和我们将要做的修改无关的内容。

然后，再次使用 `jq` 工具去追加 Org3 的配置定义，得到输出文件 `modified_config.json` 。此文件中就包含了Org1-3三个组织。

#### 2.4.3.2. 获取增量更新的`protobuf`格式文件

```bash
createConfigUpdate() {
  CHANNEL=$1
  ORIGINAL=$2
  MODIFIED=$3
  OUTPUT=$4
  configtxlator proto_encode --input "${ORIGINAL}" --type common.Config >original_config.pb
  
  configtxlator proto_encode --input "${MODIFIED}" --type common.Config >modified_config.pb
  
  configtxlator compute_update --channel_id "${CHANNEL}" --original original_config.pb --updated modified_config.pb >config_update.pb
  
  configtxlator proto_decode --input config_update.pb --type common.ConfigUpdate >config_update.json
  
  echo '{"payload":{"header":{"channel_header":{"channel_id":"'$CHANNEL'", "type":2}},"data":{"config_update":'$(cat config_update.json)'}}}' | jq . >config_update_in_envelope.json
  
  configtxlator proto_encode --input config_update_in_envelope.json --type common.Envelope >"${OUTPUT}"
}
createConfigUpdate ${CHANNEL_NAME} config.json modified_config.json org3_update_in_envelope.pb
```

首先，将 `config.json` 文件转回 protobuf 格式，命名为 `original_config.pb` 

然后，将 `modified_config.json` 编码成 `modified_config.pb`

然后，现在使用 `configtxlator` 去计算两个protobuf 配置的差异。这条命令会输出一个新的 protobuf 二进制文件，命名为` config_update.pb` 。其中包含了 Org3 的定义和指向 Org1 和 Org2 材料的更高级别的指针。我们可以抛弃 Org1 和 Org2 相关的 MSP 材料和修改策略信息，因 为这些数据已经存在于通道的初始区块。因此，我们只需要两个配置的差异部分。

然后，将` config_update.pb` 解码成可编辑的 JSON 格式，并命名为 `config_update.json` 。

然后， 需要用信封消息来包装它。这 个步骤要把之前裁剪掉的头部信息还原回来。我们将命名这个新文件为 `config_update_in_envelope.json` 。

最后，使用 `configtxlator` 工具将他转换为 Fabric 需要的完整独立的 protobuf 格式。我们将最 后的更新对象命名为 `org3_update_in_envelope.pb` 。

总体上就是得到一个增量更新的`protobuf`格式文件。

#### 2.4.3.3. MAJORITY签名并提交配置更新

```bash
# signConfigtxAsPeerOrg <org> <configtx.pb>
# Set the peerOrg admin of an org and signing the config update
signConfigtxAsPeerOrg() {
  PEERORG=$1
  TX=$2
  setGlobals $PEERORG
  peer channel signconfigtx -f "${TX}"
}
signConfigtxAsPeerOrg 1 org3_update_in_envelope.pb
setGlobals 2
peer channel update -f org3_update_in_envelope.pb -c ${CHANNEL_NAME} -o orderer.example.com:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile ${ORDERER_CA}
```

在配置写入到账本前，我们需要来自必要的 Admin 用户的签名。我们通道应用组的修改策略（mod_policy）设置为默认值 “MAJORITY”，这意味着我们需要大多数已经存在的组织管理员去签名这个更新。因为我们只有两个组织 （ Org1 和 Org2 ） 所以两个的大多数也还是两个，我们需要它们都签名。没有这两个签名，排序服务会因为不满足策略而拒绝这个交易。

Org1用`peer channel signconfigtx`对`org3_update_in_envelope.pb`签名（写回原文件），然后由Org2将`org3_update_in_envelope.pb`更新数据发送给order节点，Org2在发送前也会签名。

### 2.4.4. Org3加入channel

```bash
# Generate the needed certificates, the genesis block and start the network.
function addOrg3 () {
  echo "############### Have Org3 peers join network ##################"
  docker exec Org3cli ./scripts/org3-scripts/step2org3.sh $CHANNEL_NAME $CLI_DELAY $CLI_TIMEOUT $VERBOSE
}
```

```bash
peer channel fetch 0 $CHANNEL_NAME.block -o orderer.example.com:7050 --ordererTLSHostnameOverride orderer.example.com -c $CHANNEL_NAME --tls --cafile $ORDERER_CA >&log.txt

joinChannelWithRetry() {
  ORG=$1
  setGlobals $ORG
  
  peer channel join -b $CHANNEL_NAME.block >&log.txt
}
```

可以在这个时候才启动Org3的peer容器。

首先，使用 `peer channel fetch 0` 命令来获取通道账本上0号区块`mychannel.block`，此区块为初始化区块。

然后，执行 `peer channel join` 命令并指定初始区块 `mychannel.block` 。

同理，如果Org3有多个peer节点也同样使用`jion`加入通道即可，要操作其他peer只要改变clinet的环境变量即可。

### 2.4.5. 安装、定义和调用链码

参考`deployCC.sh`，在本地执行就可以，不用client，每个peer都要执行一次。

## 2.5. 配置文件解析

### 2.5.1. configtx文件

```yaml
# Copyright IBM Corp. All Rights Reserved.
#
# SPDX-License-Identifier: Apache-2.0
#

---
Organizations:

    # SampleOrg defines an MSP using the sampleconfig.  It should never be used
    # in production but may be used as a template for other definitions
    - &OrdererOrg
        # DefaultOrg defines the organization which is used in the sampleconfig
        # of the fabric.git development environment
        Name: OrdererOrg

        # ID to load the MSP definition as
        ID: OrdererMSP

        # MSPDir is the filesystem path which contains the MSP configuration
        MSPDir: ../organizations/ordererOrganizations/example.com/msp

        # Policies defines the set of policies at this level of the config tree
        # For organization policies, their canonical path is usually
        #   /Channel/<Application|Orderer>/<OrgName>/<PolicyName>
        Policies:
            Readers:
                Type: Signature
                Rule: "OR('OrdererMSP.member')"
            Writers:
                Type: Signature
                Rule: "OR('OrdererMSP.member')"
            Admins:
                Type: Signature
                Rule: "OR('OrdererMSP.admin')"

        OrdererEndpoints:
            - orderer.example.com:7050

    - &Org1
        # DefaultOrg defines the organization which is used in the sampleconfig
        # of the fabric.git development environment
        Name: Org1MSP

        # ID to load the MSP definition as
        ID: Org1MSP

        MSPDir: ../organizations/peerOrganizations/org1.example.com/msp

        # Policies defines the set of policies at this level of the config tree
        # For organization policies, their canonical path is usually
        #   /Channel/<Application|Orderer>/<OrgName>/<PolicyName>
        Policies:
            Readers:
                Type: Signature
                Rule: "OR('Org1MSP.admin', 'Org1MSP.peer', 'Org1MSP.client')"
            Writers:
                Type: Signature
                Rule: "OR('Org1MSP.admin', 'Org1MSP.client')"
            Admins:
                Type: Signature
                Rule: "OR('Org1MSP.admin')"
            Endorsement:
                Type: Signature
                Rule: "OR('Org1MSP.peer')"

        # leave this flag set to true.
        AnchorPeers:
            # AnchorPeers defines the location of peers which can be used
            # for cross org gossip communication.  Note, this value is only
            # encoded in the genesis block in the Application section context
            - Host: peer0.org1.example.com
              Port: 7051

    - &Org2
        # DefaultOrg defines the organization which is used in the sampleconfig
        # of the fabric.git development environment
        Name: Org2MSP

        # ID to load the MSP definition as
        ID: Org2MSP

        MSPDir: ../organizations/peerOrganizations/org2.example.com/msp

        # Policies defines the set of policies at this level of the config tree
        # For organization policies, their canonical path is usually
        #   /Channel/<Application|Orderer>/<OrgName>/<PolicyName>
        Policies:
            Readers:
                Type: Signature
                Rule: "OR('Org2MSP.admin', 'Org2MSP.peer', 'Org2MSP.client')"
            Writers:
                Type: Signature
                Rule: "OR('Org2MSP.admin', 'Org2MSP.client')"
            Admins:
                Type: Signature
                Rule: "OR('Org2MSP.admin')"
            Endorsement:
                Type: Signature
                Rule: "OR('Org2MSP.peer')"

        AnchorPeers:
            # AnchorPeers defines the location of peers which can be used
            # for cross org gossip communication.  Note, this value is only
            # encoded in the genesis block in the Application section context
            - Host: peer0.org2.example.com
              Port: 9051

################################################################################
#
#   SECTION: Capabilities
#
#   - This section defines the capabilities of fabric network. This is a new
#   concept as of v1.1.0 and should not be utilized in mixed networks with
#   v1.0.x peers and orderers.  Capabilities define features which must be
#   present in a fabric binary for that binary to safely participate in the
#   fabric network.  For instance, if a new MSP type is added, newer binaries
#   might recognize and validate the signatures from this type, while older
#   binaries without this support would be unable to validate those
#   transactions.  This could lead to different versions of the fabric binaries
#   having different world states.  Instead, defining a capability for a channel
#   informs those binaries without this capability that they must cease
#   processing transactions until they have been upgraded.  For v1.0.x if any
#   capabilities are defined (including a map with all capabilities turned off)
#   then the v1.0.x peer will deliberately crash.
#
################################################################################
Capabilities:
    # Channel capabilities apply to both the orderers and the peers and must be
    # supported by both.
    # Set the value of the capability to true to require it.
    Channel: &ChannelCapabilities
        # V2_0 capability ensures that orderers and peers behave according
        # to v2.0 channel capabilities. Orderers and peers from
        # prior releases would behave in an incompatible way, and are therefore
        # not able to participate in channels at v2.0 capability.
        # Prior to enabling V2.0 channel capabilities, ensure that all
        # orderers and peers on a channel are at v2.0.0 or later.
        V2_0: true

    # Orderer capabilities apply only to the orderers, and may be safely
    # used with prior release peers.
    # Set the value of the capability to true to require it.
    Orderer: &OrdererCapabilities
        # V2_0 orderer capability ensures that orderers behave according
        # to v2.0 orderer capabilities. Orderers from
        # prior releases would behave in an incompatible way, and are therefore
        # not able to participate in channels at v2.0 orderer capability.
        # Prior to enabling V2.0 orderer capabilities, ensure that all
        # orderers on channel are at v2.0.0 or later.
        V2_0: true

    # Application capabilities apply only to the peer network, and may be safely
    # used with prior release orderers.
    # Set the value of the capability to true to require it.
    Application: &ApplicationCapabilities
        # V2_0 application capability ensures that peers behave according
        # to v2.0 application capabilities. Peers from
        # prior releases would behave in an incompatible way, and are therefore
        # not able to participate in channels at v2.0 application capability.
        # Prior to enabling V2.0 application capabilities, ensure that all
        # peers on channel are at v2.0.0 or later.
        V2_0: true

################################################################################
#
#   SECTION: Application
#
#   - This section defines the values to encode into a config transaction or
#   genesis block for application related parameters
#
################################################################################
Application: &ApplicationDefaults

    # Organizations is the list of orgs which are defined as participants on
    # the application side of the network
    Organizations:

    # Policies defines the set of policies at this level of the config tree
    # For Application policies, their canonical path is
    #   /Channel/Application/<PolicyName>
    Policies:
        Readers:
            Type: ImplicitMeta
            Rule: "ANY Readers"
        Writers:
            Type: ImplicitMeta
            Rule: "ANY Writers"
        Admins:
            Type: ImplicitMeta
            Rule: "MAJORITY Admins"
        LifecycleEndorsement:
            Type: ImplicitMeta
            Rule: "MAJORITY Endorsement"
        Endorsement:
            Type: ImplicitMeta
            Rule: "MAJORITY Endorsement"

    Capabilities:
        <<: *ApplicationCapabilities
################################################################################
#
#   SECTION: Orderer
#
#   - This section defines the values to encode into a config transaction or
#   genesis block for orderer related parameters
#
################################################################################
Orderer: &OrdererDefaults

    # Orderer Type: The orderer implementation to start
    OrdererType: etcdraft

    EtcdRaft:
        Consenters:
        - Host: orderer.example.com
          Port: 7050
          ClientTLSCert: ../organizations/ordererOrganizations/example.com/orderers/orderer.example.com/tls/server.crt
          ServerTLSCert: ../organizations/ordererOrganizations/example.com/orderers/orderer.example.com/tls/server.crt

    # Batch Timeout: The amount of time to wait before creating a batch
    BatchTimeout: 2s

    # Batch Size: Controls the number of messages batched into a block
    BatchSize:

        # Max Message Count: The maximum number of messages to permit in a batch
        MaxMessageCount: 10

        # Absolute Max Bytes: The absolute maximum number of bytes allowed for
        # the serialized messages in a batch.
        AbsoluteMaxBytes: 99 MB

        # Preferred Max Bytes: The preferred maximum number of bytes allowed for
        # the serialized messages in a batch. A message larger than the preferred
        # max bytes will result in a batch larger than preferred max bytes.
        PreferredMaxBytes: 512 KB

    # Organizations is the list of orgs which are defined as participants on
    # the orderer side of the network
    Organizations:

    # Policies defines the set of policies at this level of the config tree
    # For Orderer policies, their canonical path is
    #   /Channel/Orderer/<PolicyName>
    Policies:
        Readers:
            Type: ImplicitMeta
            Rule: "ANY Readers"
        Writers:
            Type: ImplicitMeta
            Rule: "ANY Writers"
        Admins:
            Type: ImplicitMeta
            Rule: "MAJORITY Admins"
        # BlockValidation specifies what signatures must be included in the block
        # from the orderer for the peer to validate it.
        BlockValidation:
            Type: ImplicitMeta
            Rule: "ANY Writers"

################################################################################
#
#   CHANNEL
#
#   This section defines the values to encode into a config transaction or
#   genesis block for channel related parameters.
#
################################################################################
Channel: &ChannelDefaults
    # Policies defines the set of policies at this level of the config tree
    # For Channel policies, their canonical path is
    #   /Channel/<PolicyName>
    Policies:
        # Who may invoke the 'Deliver' API
        Readers:
            Type: ImplicitMeta
            Rule: "ANY Readers"
        # Who may invoke the 'Broadcast' API
        Writers:
            Type: ImplicitMeta
            Rule: "ANY Writers"
        # By default, who may modify elements at this config level
        Admins:
            Type: ImplicitMeta
            Rule: "MAJORITY Admins"

    # Capabilities describes the channel level capabilities, see the
    # dedicated Capabilities section elsewhere in this file for a full
    # description
    Capabilities:
        <<: *ChannelCapabilities


Profiles:

    TwoOrgsOrdererGenesis:
        <<: *ChannelDefaults
        Orderer:
            <<: *OrdererDefaults
            Organizations:
                - *OrdererOrg
            Capabilities:
                <<: *OrdererCapabilities
        Consortiums:
            SampleConsortium:
                Organizations:
                    - *Org1
                    - *Org2
    TwoOrgsChannel:
        Consortium: SampleConsortium
        <<: *ChannelDefaults
        Application:
            <<: *ApplicationDefaults
            Organizations:
                - *Org1
                - *Org2
            Capabilities:
                <<: *ApplicationCapabilities

```



# 3. 使用commercial-paper学习开发应用

## 3.1. 业务需求

这是一个商业证券的应用，涉及六个组织，这些组织通过PaperNet来发行、购买和兑换商业证券。![develop.systemscontext](imgs/hyperledger fabric/develop.diagram.1.png)

- 融资方：MagnetoCorp可以发行issue和赎回redeem证券
- 资金方：DigiBank, BigFund, BrokerHouse, HedgeMatic四个组织可以交易证券，包括买入buy、卖出sell和兑换redeem证券
- 评估方：RateM可以对证券进行监测notify和评估rate

### 3.1.1. 证券的数据结构

- ```properties
  Issuer:		发行者
  Paper:		证券号
  Owner:		持有者
  Issue date:	发行日期
  Maturity date:赎回日期
  Face value:	面额
  Current state:证券当前状态
  ```

- 其中`Current state`有三个取值：`issued`、`trading`、`redeemed`分别表示已发行、交易中、已赎回三个状态

- 证券发行时`Issuer`和`Owner`相同，都是发行人
  

### 3.1.2. 业务环节

1. 发行

   ```properties
   Txn = issue
   Issuer = MagnetoCorp
   Paper = 00001
   Issue time = 31 May 2020 09:00:00 EST
   Maturity date = 30 November 2020
   Face value = 5M USD
   ```

   这是MagnetoCorp 发行的一笔债券，参数中`Txn`表示交易类型，`Owner`就是`Issue`故不用传参。

   发行只需要发行者签名即可。

2. 购买/出售

   ```properties
   Txn = buy
   Issuer = MagnetoCorp
   Paper = 00001
   Current owner = MagnetoCorp
   New owner = DigiBank
   Purchase time = 31 May 2020 10:00:00 EST
   Price = 4.94M USD
   ```

   这里DigiBank购买了MagnetoCorp 的00001号证券，`Purchase time`和`Price`不是证券的属性，但是账本会记录这些交易信息，证券的数据结构是世界状态，要加以区分。

   购买交易需要购买者和出售者双方共体签名。

3. 赎回/兑换

   ```properties
   Txn = redeem
   Issuer = MagnetoCorp
   Paper = 00001
   Current owner = HedgeMatic
   Redeem time = 30 Nov 2020 12:00:00 EST
   ```

   这里HedgeMatic 将证券转回 MagnetoCorp。

   赎回交易需要双方共同签名。

## 3.2. 智能合约设计

### 3.2.1. 流程和数据设计

流程设计中要明确两个概念：

- 状态
- 交易

这里证券交易流程如下:![develop.statetransition](imgs/hyperledger fabric/develop.diagram.4.png)

属性的完整集合构成了商业票据的状态。这些商业票据的全部集合构成了账本的世界状态。

Hyperledger Fabric 支持不同的状态有不同的属性。

主键：PaperNet 商业票据的主键是通过 `Issuer` 属性和 `paper` 属性拼接得到的；所以 MagnetoCorp 的第一个票据的主键就是 `MagnetoCorp00001`。

### 3.2.2. 智能合约的编写

#### 3.2.2.1. java智能合约

可以使用智能合约的模板，其中使用了两个抽象类：`State.java`和`StateList.java`，分别是从ledger查询出状态对象的抽象和整个ledger的抽象。

ledger是个key-value数据库，key用户指定唯一的键，value是一个可以被序列化和反序列化的实体，调用fabric查询可以得到被序列化的二进制数组value，到应用层应该进行反序列化成为对象方便使用。通常使用json进行序列化，java中可以使用gson库。

写流程：收到请求，获取参数，封装成对象，序列化成byte数组，写入ledger，返回对象（通常返回写入ledger的对象，也可以返回写入成功的信号），代码如下：

```java
public StateList addState(State state) {
    ChaincodeStub stub = this.ctx.getStub();

    String[] splitKey = state.getSplitKey();
    CompositeKey ledgerKey = stub.createCompositeKey(this.name, splitKey);

    byte[] data = state.serialize();
    this.ctx.getStub().putState(ledgerKey.toString(), data);

    return this;
}
```

读流程：收到请求，读取ledger，得到结果，反序列化，返回对象，代码如下：

```java
public <T extends State> T getState(String key, Class<T> type) {
    CompositeKey ledgerKey = this.ctx.getStub().createCompositeKey(this.name, State.splitKey(key)); # 构建查询要用到的key对象

    byte[] data = this.ctx.getStub().getState(ledgerKey.toString()); # 查询fabric ledger得到序列化的二进制数组
    if (data != null) {
        return State.deserialize(data, type); # 反序列化得到结果
    } else {
        return null;
    }
}
```

需要注意的是：

- 存入ledger的是json序列化二进制数据
- 调用chaincode，通过网络传递结果使用的是protobuf序列化的二进制数据

## 3.3. 启动网络

### 3.3.1. 改写`netwok-start.sh`脚本

网络仍然可以继续使用`test-network`，需要启动网络可以使用`network-starter.sh`脚本，该脚本没有deployCC，即没有将chaincode发布到通道中，所以可以对文件进行部分修改，在网络启动后执行发布：

```bash
./network.sh down
./network.sh up createChannel -ca -s couchdb

# Copy the connection profiles so they are in the correct organizations.
cp "${DIR}/../test-network/organizations/peerOrganizations/org1.example.com/connection-org1.yaml" "${DIR}/../gateway/"
cp "${DIR}/../test-network/organizations/peerOrganizations/org2.example.com/connection-org2.yaml" "${DIR}/../gateway/"

./network.sh deployCC -l java
```

`gateway`文件夹中的文件作用是api调用fabric时，需要建立gateway连接，这些文件用于连接到对应peer节点已经order节点，每次启动新的网络由于网络连接的证书文件变了，所以这些文件也要重新导入，如果使用错误的连接文件会导致无法连接网络。

org1生成的连接文件如下：

```yaml
---
name: test-network-org1
version: 1.0.0
client:
  organization: Org1
  connection:
    timeout:
      peer:
        endorser: '300'
organizations:
  Org1:
    mspid: Org1MSP
    peers:
    - peer0.org1.example.com
    certificateAuthorities:
    - ca.org1.example.com
peers:
  peer0.org1.example.com:
    url: grpcs://localhost:7051
    tlsCACerts:
      pem: |
          -----BEGIN CERTIFICATE-----
          MIICJjCCAc2gAwIBAgIUeuVNIUBmOnA5PnI9W3bHeeNHJNkwCgYIKoZIzj0EAwIw
          cDELMAkGA1UEBhMCVVMxFzAVBgNVBAgTDk5vcnRoIENhcm9saW5hMQ8wDQYDVQQH
          EwZEdXJoYW0xGTAXBgNVBAoTEG9yZzEuZXhhbXBsZS5jb20xHDAaBgNVBAMTE2Nh
          Lm9yZzEuZXhhbXBsZS5jb20wHhcNMjAwNzE0MTQ1NjAwWhcNMzUwNzExMTQ1NjAw
          WjBwMQswCQYDVQQGEwJVUzEXMBUGA1UECBMOTm9ydGggQ2Fyb2xpbmExDzANBgNV
          BAcTBkR1cmhhbTEZMBcGA1UEChMQb3JnMS5leGFtcGxlLmNvbTEcMBoGA1UEAxMT
          Y2Eub3JnMS5leGFtcGxlLmNvbTBZMBMGByqGSM49AgEGCCqGSM49AwEHA0IABGfh
          6zLb80mYpEPHqbw2enW3gk6faCFijLiqAoAWoKARD6b7KaMOliQYjZqg/hoPoKfK
          vungWXIjIbakjOewI0ejRTBDMA4GA1UdDwEB/wQEAwIBBjASBgNVHRMBAf8ECDAG
          AQH/AgEBMB0GA1UdDgQWBBR47gGJpB6OjjMXxYnpMIBrtrol/zAKBggqhkjOPQQD
          AgNHADBEAiByb1BYRuFcxsKxwiPmEkO9Ff6MYgmeBU3TYfd3qUPW0QIgHXMuZHnN
          aDq2a6F46p2GxzwXbeBFnCRaunfIcakYEf4=
          -----END CERTIFICATE-----
          
    grpcOptions:
      ssl-target-name-override: peer0.org1.example.com
      hostnameOverride: peer0.org1.example.com
certificateAuthorities:
  ca.org1.example.com:
    url: https://localhost:7054
    caName: ca-org1
    tlsCACerts:
      pem: 
        - |
          -----BEGIN CERTIFICATE-----
          MIICJjCCAc2gAwIBAgIUeuVNIUBmOnA5PnI9W3bHeeNHJNkwCgYIKoZIzj0EAwIw
          cDELMAkGA1UEBhMCVVMxFzAVBgNVBAgTDk5vcnRoIENhcm9saW5hMQ8wDQYDVQQH
          EwZEdXJoYW0xGTAXBgNVBAoTEG9yZzEuZXhhbXBsZS5jb20xHDAaBgNVBAMTE2Nh
          Lm9yZzEuZXhhbXBsZS5jb20wHhcNMjAwNzE0MTQ1NjAwWhcNMzUwNzExMTQ1NjAw
          WjBwMQswCQYDVQQGEwJVUzEXMBUGA1UECBMOTm9ydGggQ2Fyb2xpbmExDzANBgNV
          BAcTBkR1cmhhbTEZMBcGA1UEChMQb3JnMS5leGFtcGxlLmNvbTEcMBoGA1UEAxMT
          Y2Eub3JnMS5leGFtcGxlLmNvbTBZMBMGByqGSM49AgEGCCqGSM49AwEHA0IABGfh
          6zLb80mYpEPHqbw2enW3gk6faCFijLiqAoAWoKARD6b7KaMOliQYjZqg/hoPoKfK
          vungWXIjIbakjOewI0ejRTBDMA4GA1UdDwEB/wQEAwIBBjASBgNVHRMBAf8ECDAG
          AQH/AgEBMB0GA1UdDgQWBBR47gGJpB6OjjMXxYnpMIBrtrol/zAKBggqhkjOPQQD
          AgNHADBEAiByb1BYRuFcxsKxwiPmEkO9Ff6MYgmeBU3TYfd3qUPW0QIgHXMuZHnN
          aDq2a6F46p2GxzwXbeBFnCRaunfIcakYEf4=
          -----END CERTIFICATE-----
          
    httpOptions:
      verify: false

```

主要包括org1的peer节点、ca机构的url，连接使用tls加密通信使用的x.509证书，它们使用同一个证书。

### 3.3.2. 改写`deployCC.sh`脚本

此文件位于`test-network/scripts/`目录下，由于发布到channel的chaincode是新编写的，而`deployCC.sh`中发布的是`fabcar`链码，所以应该改写此文件，如下：

```bash
CONTRACT_DIRECTORY="magnetocorp"

if [ "$CC_SRC_LANGUAGE" = "go" -o "$CC_SRC_LANGUAGE" = "golang" ] ; then
	CC_RUNTIME_LANGUAGE=golang
	CC_SRC_PATH="../commercial-paper/organization/${CONTRACT_DIRECTORY}/contract-go"

	echo Vendoring Go dependencies ...
	pushd $CC_SRC_PATH
	GO111MODULE=on go mod vendor
	popd
	echo Finished vendoring Go dependencies

elif [ "$CC_SRC_LANGUAGE" = "javascript" ]; then
	CC_RUNTIME_LANGUAGE=node # chaincode runtime language is node.js
	CC_SRC_PATH="../commercial-paper/organization/${CONTRACT_DIRECTORY}/contract"

elif [ "$CC_SRC_LANGUAGE" = "java" ]; then
	CC_RUNTIME_LANGUAGE=java
	CC_SRC_PATH="../commercial-paper/organization/${CONTRACT_DIRECTORY}/contract-java/build/install/papercontract"

	echo Compiling Java code ...
	pushd ../commercial-paper/organization/${CONTRACT_DIRECTORY}/contract-java
	./gradlew installDist
	popd
	echo Finished compiling Java code

else
	echo The chaincode language ${CC_SRC_LANGUAGE} is not supported by this script
	echo Supported chaincode languages are: go, java, javascript, and typescript
	exit 1
fi

#sleep 10
## Query chaincode on peer0.org1
#echo "Querying chaincode on peer0.org1..."
#chaincodeQuery 1
```

主要改写三部分：

1. 编译链码部分，指定正确的源码位置
2. 打包、发布链码等使用到的链码名，最好使用一个变量统一替换
3. 最后链码测试部分，只需要保留链码初始化

### 3.3.3. 网络监控

使用`commercial-paper/organization/d/configuration/cli`中的`monitordocker.sh`可以启动一个`hyperledger/fabric-tools`的docker容器监控所有在网络中的容器的日志，从而在chaincode出错时可以查看日志排错。

监控具体容器可以使用：

```bash
docker logs [img]
```

### 3.3.4. 启动脚本

测试时使用两个脚本：`network-clean.sh` 和 `network-starter.sh` 脚本，用于关闭和启动网络。

1. `network-clean`文件

```bash
function _exit(){
    printf "Exiting:%s\n" "$1"
    exit -1
}

set -ev
set -o pipefail

# Where am I?
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

export FABRIC_CFG_PATH="${DIR}/../config"

# 这两个文件夹必须在启动网络后生成，每次生成的都不一样
rm -rf ${DIR}/../gateway/*
rm -rf ${DIR}/../wallet/*

cd "${DIR}/../test-network/"

./network.sh down

# remove any stopped containers
docker rm $(docker ps -aq)
service docker restart
```

脚本中主要清除gateway文件夹中的连接配置文件和wallet中连接账户的证书文件，由于这两个文件每次启动网络是不同的。

其次`./network.sh down`执行会清除启动网络，创建channel，发布chaincode生成的文件。

最后重启docker防止意外错误。

2. `network-start`文件

```bash
#!/bin/bash
#
# SPDX-License-Identifier: Apache-2.0

function _exit(){
    printf "Exiting:%s\n" "$1"
    exit -1
}

# Exit on first error, print all commands.
set -ev
set -o pipefail

# Where am I? 这里定位的是脚本存放的目录，直接用 `pwd` 在执行脚本时不再脚本目录会出问题
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

export FABRIC_CFG_PATH="${DIR}/../config"

cd "${DIR}/../test-network/"

./network.sh down
./network.sh up createChannel -ca -s couchdb

# Copy the connection profiles so they are in the correct organizations.
cp "${DIR}/../test-network/organizations/peerOrganizations/org1.example.com/connection-org1.yaml" "${DIR}/../gateway/"
cp "${DIR}/../test-network/organizations/peerOrganizations/org2.example.com/connection-org2.yaml" "${DIR}/../gateway/"

cp ${DIR}/../test-network/organizations/peerOrganizations/org1.example.com/users/User1@org1.example.com/msp/signcerts/* ${DIR}/../test-network/organizations/peerOrganizations/org1.example.com/users/User1@org1.example.com/msp/signcerts/User1@org1.example.com-cert.pem
cp ${DIR}/../test-network/organizations/peerOrganizations/org1.example.com/users/User1@org1.example.com/msp/keystore/* ${DIR}/../test-network/organizations/peerOrganizations/org1.example.com/users/User1@org1.example.com/msp/keystore/priv_sk

cp ${DIR}/../test-network/organizations/peerOrganizations/org2.example.com/users/User1@org2.example.com/msp/signcerts/* ${DIR}/../test-network/organizations/peerOrganizations/org2.example.com/users/User1@org2.example.com/msp/signcerts/User1@org2.example.com-cert.pem
cp ${DIR}/../test-network/organizations/peerOrganizations/org2.example.com/users/User1@org2.example.com/msp/keystore/* ${DIR}/../test-network/organizations/peerOrganizations/org2.example.com/users/User1@org2.example.com/msp/keystore/priv_sk

echo Suggest that you monitor the docker containers by running
echo "./organization/magnetocorp/configuration/cli/monitordocker.sh net_test"

./network.sh deployCC -l java
```

文件中启动了网络，创建了channel。

然后复制网络连接配置文件到`gateway`目录中是为了后面应用调用时使用方便找到，实际开发可能需要将此文件统一上传到应用服务器。

复制`.pem`和`priv_sk`是后面应用可以用来生成wallet文件，`.pem`是x.509公钥签名数字证书用于证明登录者身份已经提供公钥，`priv_sk`是私钥文件，用于对提交的请求进行签名。（==大概流程是：用户使用gateway中配置可以和peer建立tls加密连接，用户发送经济`priv_sk`签名的请求到peer节点，并附带`.pem`证书，peer受到请求验证证书证明用户身份并获得用户的公钥，使用公钥验证请求签名，签名合法证明请求有效就执行请求。==）

最后发布chaincode。

## 3.4. 开发app、调用chaincode

### 3.4.1. Java App开发

开发只需要依赖`fabric-gateway-java`即可，通常调用链码返回结果是链码中使用的对象类型，所以还需要依赖`fabric-chaincode-shim`中的一些注解，不用具体它的函数，还需要json解析。依赖如下，注意fabric的docker容器版本2.0.0，而gateway的版本是2.1.0：

```xml
<properties>
    <!-- fabric-chaincode-java -->
    <fabric-chaincode-java.version>[2.0,3.0)</fabric-chaincode-java.version>
    <!-- fabric-gateway-java -->
    <fabric-gateway-java.version>2.1.0</fabric-gateway-java.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.hyperledger.fabric</groupId>
        <artifactId>fabric-gateway-java</artifactId>
        <version>${fabric-gateway-java.version}</version>
    </dependency>

    <!-- Used for datatype annotations only -->
    <dependency>
        <groupId>org.hyperledger.fabric-chaincode-java</groupId>
        <artifactId>fabric-chaincode-shim</artifactId>
        <version>${fabric-chaincode-java.version}</version>
        <scope>compile</scope>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.json/json -->
    <dependency>
        <groupId>org.json</groupId>
        <artifactId>json</artifactId>
        <version>20180813</version>
    </dependency>

</dependencies>
```

java开发有一定的局限性，`fabric-gateway-java`不可以连接ca，实际上`fabric-gateway-java`依赖了`fabric-sdk-java`，其中包括了ca的连接类，但是不推荐使用，可能是不太稳定，所以不像node.js sdk，java sdk无法开发稳定的通过ca注册新用户的服务，通常使用cryptogen工具生成的用户证书。

这些内置用户的证书可以使用`AddToWallet.java`工具类打包到一个文件中，这个文件被称为钱包wallet文件，该类代码如下：

```java
private static X509Certificate readX509Certificate(final Path certificatePath) throws IOException, CertificateException {
    try (Reader certificateReader = Files.newBufferedReader(certificatePath, StandardCharsets.UTF_8)) {
        return Identities.readX509Certificate(certificateReader);
    }
}

private static PrivateKey getPrivateKey(final Path privateKeyPath) throws IOException, InvalidKeyException {
    try (Reader privateKeyReader = Files.newBufferedReader(privateKeyPath, StandardCharsets.UTF_8)) {
        return Identities.readPrivateKey(privateKeyReader);
    }
}

public static void main(String[] args) {
    try {
        // A wallet stores a collection of identities
        Path walletPath = Paths.get(".", "wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);

        Path credentialPath = Paths.get("." ,"test-network", "organizations",
                                        "peerOrganizations", "org2.example.com", "users", "User1@org2.example.com", "msp");
        System.out.println("credentialPath: " + credentialPath.toString());
        Path certificatePath = credentialPath.resolve(Paths.get("signcerts",
                                                                "User1@org2.example.com-cert.pem"));
        System.out.println("certificatePem: " + certificatePath.toString());
        Path privateKeyPath = credentialPath.resolve(Paths.get("keystore",
                                                               "priv_sk"));

        X509Certificate certificate = readX509Certificate(certificatePath);
        PrivateKey privateKey = getPrivateKey(privateKeyPath);

        Identity identity = Identities.newX509Identity("Org2MSP", certificate, privateKey);


        String identityLabel = "User1@org2.example.com";
        wallet.put(identityLabel, identity);

        System.out.println("Write wallet info into " + walletPath.toString() + " successfully.");

    } catch (IOException | CertificateException | InvalidKeyException e) {
        System.err.println("Error adding to wallet");
        e.printStackTrace();
    }
}
```

主要是将test-network中使用cryptogen工具生成的`.pem`和`priv_sk`文件打包，并附带上用户名，得到Identity，然后用Wallet工具类保存到wallet文件中。



调用链码过程是通过wallet生成Identity，再通过connect配置传入Identity可以连接网络上的peer节点，得到Gateway对象就可以进行链码调用了。代码如下：

```java
private static final String ENVKEY = "CONTRACT_NAME";

public static void main(String[] args) {

    String contractName = "paper";
    // get the name of the contract, in case it is overridden
    Map<String, String> envvar = System.getenv();
    if (envvar.containsKey(ENVKEY)) {
        contractName = envvar.get(ENVKEY);
    }

    Gateway.Builder builder = Gateway.createBuilder();
    Gateway.Builder builder2 = Gateway.createBuilder();

    try {
        // A wallet stores a collection of identities
        Path walletPath = Paths.get(".", "wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        System.out.println("Read wallet info from: " + walletPath.toString());

        String userName = "User1@org2.example.com";

        Path connectionProfile = Paths.get(".", "gateway", "connection-org2.yaml");

        Path org2 = Files.list(walletPath).filter(path -> path.toString().contains("org2")).findAny().get();
        String s = Files.readString(org2);
        JsonElement parse = new JsonParser().parse(s);
        String certificate = parse.getAsJsonObject().get("credentials").getAsJsonObject().get("certificate").getAsString();
        String privateKey = parse.getAsJsonObject().get("credentials").getAsJsonObject().get("privateKey").getAsString();
        X509Identity identity = Identities.newX509Identity("Org2MSP", Identities.readX509Certificate(certificate), Identities.readPrivateKey(privateKey));

        // Set connection options on the gateway builder
        builder.identity(wallet, userName).networkConfig(connectionProfile).discovery(false);
        // Set connection options on the gateway builder
        				builder2.identity(identity).networkConfig(connectionProfile).discovery(false);

        // Connect to gateway using application specified parameters
        try (Gateway gateway = builder2.connect()) {
            // Access PaperNet network
            System.out.println("Use network channel: mychannel.");
            Network network = gateway.getNetwork("mychannel");

            // Get addressability to commercial paper contract
            System.out.println("Use org.papernet.commercialpaper smart contract.");
            Contract contract = network.getContract(contractName, "org.papernet.commercialpaper");

            // Issue commercial paper
            System.out.println("Submit commercial paper issue transaction.");
            byte[] response = contract.submitTransaction("issue", "MagnetoCorp", "00005", "2020-05-31", "2020-11-30", "5000000");

            // Process response
            System.out.println("Process issue transaction response.");
            CommercialPaper paper = CommercialPaper.deserialize(response);
            System.out.println(paper);
        }
    } catch (GatewayException | IOException | TimeoutException | InterruptedException e) {
        e.printStackTrace();
        System.exit(-1);
    } catch (CertificateException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    }
}
```

# 4. fabric1.0源码

源码下载

首先先创建源码目录（注意这里必须是gopath下的这个目录，否则编译不过）

```bash
$ mkdir -p $GOPATH/src/github.com/hyperledger
$ cd $GOPATH/src/github.com/hyperledger
```

3、编译

在源码目录中make

不过这样要求比较高，耗时比较长，会从网上下载一些docker镜像等等，读者如果嫌费时间或者由于网络原因搞不定，可以一个一个make

比如make native是编译一些bin比如order，peer等

而make order和make peer是编译两个关键程序

由于编译不是本文的重点所以简单提一下，如果有疑问欢迎给笔者留言交流

二、运行

自己配置联盟比较麻烦，比较建议是下载一个开源项目使用docker运行，以下是简单的方式

1、环境要求：

安装docker-compose

2、下载开源项目

$ git clone [https://github.com/yeasy/dock...](https://github.com/yeasy/docker-compose-files)
$ cd docker-compose-files/hyperledger_fabric/v1.0.0

3、下载镜像

$ sudo bash scripts/ download_images.sh

4、启动

$ make start

后面的具体方式可以看Makefile，有运行、停止、初始化、测试链码等命令

三、架构

联盟链的概念比较多，从比特币转过来的同学可能刚开始会晕（因为我当时研究的时候看各种文章就很晕），所以我这里尽量把我的理解路径写出来

1、主程序

联盟链真正运行时只有两个主程序

1）peer，这个程序是参与实体的运行终端，也是命令行的终端（客户端），可以理解为比特币中的主程序。

2）order，一个排序服务，类比到比特币，暂时简单理解为主程序中的挖矿部分，因为联盟链把这部分功能单独隔离了出来。随着代码的深入了解，可以发掘其其他功能。

2、讲解模型

其中order服务是一个单独的服务，可以理解为一个专门各个组织公认的权威服务器（也可以是服务集群），主要用来对交易进行排序，然后生成区块（挖矿）

Org代表组织，讲解的模型中有两个组织，org1和org2，可以对应的是清华大学和北京大学

而peer一般代表的是组织内的子部门，比如org1.peer1代表清华大学的计算机学院，org2.peer2代表北京大学的计算机学院。

3、启动命令行

对应上图中的讲解模型每一个节点中的运行的命令行如下

1）order服务：order start

2）org1.peer1 : peer node start

3）org2.peer1 : peer node start

4）org1.peer2:peer node start

5）org2.peer2:peer node start

这里除了order运行是order start以外其他的全部是peer node start，那怎么区分他们的功能呢？

答案是通过msp和tls两个目录（这个后续再继续讲）

在各个节点运行过后，网络是这样的

也就是网络其实还没有互联起来

4、网络互联

要完成网络需要三步

1）创建通道

创建通道，可以单独在一个机器上运行

peer channel create
-o orderer.example.com:7050
-c testchannel
-f ./ testchannel.tx
ps：为了简单，这里省略了一些证书信息的配置（后文会讲解）

其中当前需要理解的参数是-o orderer.example.com:7050 代表了order的url，-c testchannel代表了通道的名字

这个命令其实就是通过连接到orderer.example.com:7050服务上，让服务器增加了一个通道的配置（配置文件是testchannel.tx），这个配置中包括了一些信息，比如证书信息，机构信息，以及一些权限设置等等。然后order会返回一个testchannel.block的文件（也是一些配置信息），加入通道时候会用到。

这一步并没有构建网络，网络模型依然如下

只是order服务中多了一个叫testchannel的通道

2）加入通道

命令为

CORE_PEER_ADDRESS=peer1.org1.example.com:7051
peer channel join
-b testchannel.block
ps：这里省略了msp配置的信息

主要参数列出来

1、CORE_PEER_ADDRESS表明要加入通道的地址（因为实际操作时候是用的cli在另外的机器上操作要加入通道的peer）

2、testchannel.block 创建通道时候的返回文件

该命令执行完以后，网络模型变成了

可以看到org1的peer1和order连接陈功了，依次执行

CORE_PEER_ADDRESS=peer2.org1.example.com:7051
peer channel join
-b testchannel.block

CORE_PEER_ADDRESS=peer1.org2.example.com:7051
peer channel join
-b testchannel.block

CORE_PEER_ADDRESS=peer2.org2.example.com:7051
peer channel join
-b testchannel.block

最后网络模型变成了

3）设置锚节点

各个节点都加入网络后，细心的读者可能会发现，和我们要达到的网络模型还少了几条线，也就是各个节点的互通线没有连接。而这个的完成需要gossip协议，而gossip协议是一个节点相互发现的协议，其中有一项就是需要一些锚点，fabric中一般每一个组织一到多个锚点。这个概念在讲解gossip协议的时候会进一步说明，在这里就只需要知道锚点的设置是为了网络互通就可以了，实在要理解，暂时理解为p2p协议中的初始域名服务器。命令如下：

peer channel update
-o orderer.example.com:7050
-c testchannel
-f ./Org1MSPanchors.tx \
其中-c代表了通道名称，-f表示了锚点信息（里边会包含锚点的url比如peer1）-o指定了order节点。

在org1和org2中中分别生成锚点配置文件，然后执行以上命令就能把锚点信息更新到通道的配置中，这样各个节点就可以通过从order中更新配置，知道锚点，然后通过gossip协议进行全网互联，互联后网络模型如下



















































































