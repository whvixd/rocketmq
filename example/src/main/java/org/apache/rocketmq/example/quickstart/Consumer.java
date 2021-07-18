/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rocketmq.example.quickstart;

import java.util.List;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import static org.apache.rocketmq.common.protocol.heartbeat.MessageModel.BROADCASTING;
import static org.apache.rocketmq.common.protocol.heartbeat.MessageModel.CLUSTERING;

/**
 * This example shows how to subscribe and consume messages using providing {@link DefaultMQPushConsumer}.
 */
public class Consumer {

    public static void main(String[] args) throws InterruptedException, MQClientException {
        initConfig();
        /*
         * Instantiate with specified consumer group name.
         */
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("whvixd_local_debug_consumer");

        consumer.setNamesrvAddr("127.0.0.1:9876");
        /*
         * Specify name server addresses.
         * <p/>
         *
         * Alternatively, you may specify name server addresses via exporting environmental variable: NAMESRV_ADDR
         * <pre>
         * {@code
         * consumer.setNamesrvAddr("name-server1-ip:9876;name-server2-ip:9876");
         * }
         * </pre>
         */

        /*
         * Specify where to start in case the specified consumer group is a brand new one.
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
//        consumer.setMessageModel(BROADCASTING);

        /*
         * Subscribe one more more topics to consume.
         */
        consumer.subscribe("TopicTest", "*");

        /*
         *  Register callback to execute on arrival of messages fetched from brokers.
         */
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                ConsumeConcurrentlyContext context) {
//                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                System.out.printf("commitLogOffset: %d, Body: %s %n", msgs.get(0).getCommitLogOffset(), new String(msgs.get(0).getBody()));

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        /*
         * org.apache.rocketmq.client.impl.consumer.RebalanceService.run 入口
         * 1. this.pullMessageService.start();
         * 2. PullMessageService -> run() 轮询拉取堵塞队列
         * 3. PullCallback -> onSuccess()
         * 4. DefaultMQPushConsumerImpl.this.consumeMessageService.submitConsumeRequest()
         * 5. ConsumeRequest->run()
         * 6. status = listener.consumeMessage(Collections.unmodifiableList(msgs), context);
         * 7. Consumer执行69行代码
         *  Launch the consumer instance.
         */
        consumer.start();
        /**
         * todo：offset如何算出的
         */

        System.out.printf("Consumer Started.%n");
    }

    private static void initConfig(){
        // 广播模式offset本地存储目录
        System.setProperty("rocketmq.client.localOffsetStoreDir","/Users/didi/Documents/workspace/idea/rocketmq/localdebug/store/offsets");
    }
}



/*
/Users/didi/.rocketmq_offsets/192.168.1.14@DEFAULT/whvixd_local_debug_consumer/offsets.json
广播模式，默认存储到本地的目录

brokerName+queueId+topic:offset
{
	"offsetTable":{
		{
			"brokerName":"broker-a",
			"queueId":3,
			"topic":"TopicTest"
		}:1250,
		{
			"brokerName":"broker-a",
			"queueId":2,
			"topic":"TopicTest"
		}:1249,
		{
			"brokerName":"broker-a",
			"queueId":1,
			"topic":"TopicTest"
		}:1247,
		{
			"brokerName":"broker-a",
			"queueId":0,
			"topic":"TopicTest"
		}:1250
	}
}

 */
