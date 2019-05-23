# 实现bitcoin协议

一直以来都认为区块链技术是潜力股，自己也想向这个方向发展，因此先做一些技术储备。bitcoin是基于区块链技术的第一代产品，在此斗胆自己实现bitcoin协议，因为自己最擅长的语言是java，所以先基于java实现协议，后续再考虑go语言

## 实现思路
我的实现思路非常简单，依据bitcoin wiki `https://en.bitcoin.it/wiki/Protocol_documentation`中的协议规范做代码层的实现，目前只实现协议中的核心部分和关键内容，特别严谨的逻辑及步骤后续完善，目前的第一要务是把协议的基础模型构建出来，主要包含如下内容：

### P2P Network
P2P网络是bitcoin协议的通讯模型，基于bitcoin的公链性质，任何的Peer都可以加入到网络中。我这里采用Netty实现各Peer通讯的接收和发送。
+ `NettyServer NettyClient` 实现`com.google.common.util.concurrent.Service`接口进行状态管理
+  `MessageDecoder` extends `io.netty.handler.codec.ByteToMessageDecoder`将接受的byte反序列化为`Message`
+ `MessageEncoder` extends `io.netty.handler.codec.MessageToByteEncoder`将`Message`序列化为byte

通过`NettyServer NettyClient`可以与网络中的其他Peer链接，`NettyServer`接收其他Peer的Message（Block, Transaction, Ping ...)，直接丢给对应的Message处理并响应。此处实现了消息接收和处理的解耦。

### Message model
```
                   +-------+
                   |Message| -----+-------+--------+------+
                   +-------+      |       |        |      |
                       |          |       |        |      |
                       |          |       |        |      |
                +------------+ +----+ +-------+ +-----+ +---+
                |ChildMessage| |Ping| |Version| |Block| |...|
                +------------+ +----+ +-------+ +-----+ +---+
                       |
      +----------------+------------------+------------+
      |                |                  |            |
+-----------+ +----------------+ +-----------------+ +---+
|Transaction| |TransactionInput| |TransactionOutput| |...|
+-----------+ +----------------+ +-----------------+ +---+
```

+ `Message`bitcoin协议报文的隐喻，我的协议实现的基类，规范了协议实现的模板。着重介绍2个方法`parse()`将二进制报文规范反序列化为Message Object， `serialize()`将Message Object序列化为可传输的二进制bit

+ `Block`实现[https://en.bitcoin.it/wiki/Protocol_documentation#block](https://en.bitcoin.it/wiki/Protocol_documentation#block)Block协议，实现了Merkle数root计算、工作量证明检查、Block Hash值计算、难度系数计算、挖矿等基本操作

+ `Transaction TransactionInput TransactionOutput TransactionOutPoint`实现[https://en.bitcoin.it/wiki/Protocol_documentation#tx](https://en.bitcoin.it/wiki/Protocol_documentation#tx)Tx协议，三个对象共同完成协议的组装机验证



### Script and contract
依据协议内容[https://en.bitcoin.it/wiki/Script](https://en.bitcoin.it/wiki/Script)设计一个基于Stack简单脚本执行器，处理Stack操作，流程控制、字符操作、逻辑操作、算数操作、加密操作等，通过该脚本的执行实现交易权限检查（保证某用户只能花费自己的交易）及简单的智能合约。
### Transaction Broadcast
待实现
### Block broadcast and block chain
待实现

# 参考资料
