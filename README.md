# FileChain：HaoFS的Fabric智能合约和测试环境

## 介绍

- 项目中 `FileChain` 是HaoFS项目使用的智能合约

- `test-network` 目录下是启动测试用的Fabric网络，使用的是修改后的fabric-sample/test-network模板，目录下的 `network-start.sh` 脚本可以一键启动三个组织的Fabric网络，扩展更多组织可以参考`test-network/add-Org3`

## 使用

- ```
  .
  |- config         其中是fabric的默认配置文件
  |- chaincode      其中存放链码，此项目的链码是java写的
  |- gateway        其中存放连接fabric的配置文件，文件从test-network中生成，每次启动生成不同的
  |- wallet         其中存放操作fabric网络需要的证书，fabric的身份机制参考fabric官方文档
  
  |- bootstrap.sh       下载启动网络所需要的docker进行和配置文件
  |- network-clean.sh   完全清除网络
  |- network-starter.sh 启动两个组织的网络
  |- hyperledger fabric.md  学习fabric的教程，介绍了原始test-network干了什么
  ```
