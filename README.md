Example of integration to HornetQ integration, where HornetQ is embedded JBoss.

Answer to question 

http://stackoverflow.com/questions/18820007/spring-web-to-connect-to-hornetq-jms-embeded-with-jboss-server-7-1-1

Pre-configuration for messaging in JBoss:

```xml
        <extension module="org.jboss.as.messaging"/>

        <!-- ... -->    

        <subsystem xmlns="urn:jboss:domain:messaging:1.1">
            <hornetq-server>
                <persistence-enabled>true</persistence-enabled>
                <journal-file-size>102400</journal-file-size>
                <journal-min-files>2</journal-min-files>
                <!-- javax.jms.JMSSecurityException: Unable to validate user: null if this is not set: -->
                <security-enabled>false</security-enabled>
    
                <connectors>
                    <netty-connector name="netty" socket-binding="messaging"/>
                    <netty-connector name="netty-throughput" socket-binding="messaging-throughput">
                        <param key="batch-delay" value="50"/>
                    </netty-connector>
                    <in-vm-connector name="in-vm" server-id="0"/>
                </connectors>
    
                <acceptors>
                    <netty-acceptor name="netty" socket-binding="messaging"/>
                    <netty-acceptor name="netty-throughput" socket-binding="messaging-throughput">
                        <param key="batch-delay" value="50"/>
                        <param key="direct-deliver" value="false"/>
                    </netty-acceptor>
                    <in-vm-acceptor name="in-vm" server-id="0"/>
                </acceptors>
    
                <security-settings>
                    <security-setting match="#">
                        <permission type="send" roles="jmsrole guest"/>
                        <permission type="consume" roles="jmsrole guest"/>
                        <permission type="createNonDurableQueue" roles="jmsrole guest"/>
                        <permission type="deleteNonDurableQueue" roles="jmsrole guest"/>
                    </security-setting>
                </security-settings>
    
                <address-settings>
                    <address-setting match="#">
                        <dead-letter-address>jms.queue.DLQ</dead-letter-address>
                        <expiry-address>jms.queue.ExpiryQueue</expiry-address>
                        <redelivery-delay>0</redelivery-delay>
                        <max-size-bytes>10485760</max-size-bytes>
                        <address-full-policy>BLOCK</address-full-policy>
                        <message-counter-history-day-limit>1</message-counter-history-day-limit>
                    </address-setting>
                </address-settings>
    
                <jms-connection-factories>
                    <connection-factory name="InVmConnectionFactory">
                        <connectors>
                            <connector-ref connector-name="in-vm"/>
                        </connectors>
                        <entries>
                            <entry name="java:/ConnectionFactory"/>
                        </entries>
                    </connection-factory>
                    <connection-factory name="RemoteConnectionFactory">
                        <connectors>
                            <connector-ref connector-name="netty"/>
                        </connectors>
                        <entries>
                            <entry name="RemoteConnectionFactory"/>
                            <entry name="java:jboss/exported/jms/RemoteConnectionFactory"/>
                        </entries>
                    </connection-factory>
                    <pooled-connection-factory name="hornetq-ra">
                        <transaction mode="xa"/>
                        <connectors>
                            <connector-ref connector-name="in-vm"/>
                        </connectors>
                        <entries>
                            <entry name="java:/JmsXA"/>
                        </entries>
                    </pooled-connection-factory>
                </jms-connection-factories>
    
                <jms-destinations>
                    <jms-queue name="notificationQueue">
                        <entry name="queue/notificationQueue"/>
                        <entry name="java:jboss/exported/jms/queue/notificationQueue"/>
                    </jms-queue>
                    <jms-queue name="videoConversionQueue">
                        <entry name="queue/videoConversionQueue"/>
                        <entry name="java:jboss/exported/jms/queue/videoConversionQueue"/>
                    </jms-queue>
                    <jms-queue name="contentEventQueue">
                        <entry name="queue/contentEventQueue"/>
                        <entry name="java:jboss/exported/jms/queue/contentEventQueue"/>
                    </jms-queue>
                    <jms-queue name="DemoQueue">
                      <entry name="queue/DemoQueue" />
                    </jms-queue>
                    <jms-topic name="testTopic">
                        <entry name="topic/test"/>
                        <entry name="java:jboss/exported/jms/topic/test"/>
                    </jms-topic>
                </jms-destinations>
            </hornetq-server>
        </subsystem>
        <!-- ... -->    
        <socket-binding name="messaging" port="5445"/>
        <socket-binding name="messaging-throughput" port="5455"/>
```