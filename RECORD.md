1. **localdebug** 为本地调式目录，包含abort、conf、logs、store四个目录

2. conf中包含`broker.conf`、`logback_namesrv.xml`两个配置文件，可从`distribution/conf`复制过来

3. 其中broker.conf配置修改如下：

```config
brokerClusterName = DefaultCluster
brokerName = broker-a
brokerId = 0
### 这是nameserver的地址
namesrvAddr=127.0.0.1:9876
deleteWhen = 04
fileReservedTime = 48
brokerRole = ASYNC_MASTER
flushDiskType = ASYNC_FLUSH 
# 这是存储路径，你设置为/Users/didi/Documents/workspace/idea/rocketmq/localdebug/store
storePathRootDir=/Users/didi/Documents/workspace/idea/rocketmq/store
# 这是commitLog的存储路径
storePathCommitLog=/Users/didi/Documents/workspace/idea/rocketmq/localdebug/store/commitlog
# consume queue文件的存储路径
storePathConsumeQueue=/Users/didi/Documents/workspace/idea/rocketmq/localdebug/store/consumequeue
# 消息索引文件的存储路径
storePathIndex=/Users/didi/Documents/workspace/idea/rocketmq/localdebug/store/index
# checkpoint文件的存储路径
storeCheckpoint=/Users/didi/Documents/workspace/idea/rocketmq/localdebug/store/checkpoint
# abort文件的存储路径
abortFile=/Users/didi/Documents/workspace/idea/rocketmq/localdebug/abort
```


4. **logback_namesrv.xml**修改`${user.home}`为`/Users/didi/Documents/workspace/idea/rocketmq/localdebug`
**logback_broker.xml**类似

5. namesrv和broker启动终端添加变量：`ROCKETMQ_HOME=/Users/didi/Documents/workspace/idea/rocketmq/localdebug`

6. broker终端program arguments添加 `-c /Users/didi/Documents/workspace/idea/rocketmq/localdebug/conf/broker.conf`

7. 启动顺序：namesrv -> broker -> consumer -> producer