package gr.gnostix.kafka.connect.asterisk.source;

import gr.gnostix.kafka.connect.asterisk.config.AsteriskAmiConnectorConfig;
import gr.gnostix.kafka.connect.asterisk.config.Version;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.asteriskjava.manager.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by gnostix on 15/5/2018.
 */
public class AsteriskAmiSourceTask extends SourceTask {
    private static final Logger logger = LoggerFactory.getLogger(AsteriskAmiSourceTask.class);
    private ConcurrentLinkedQueue<SourceRecord> queueRecords;
    private ManagerConnection managerConnection;
    private int batchSize;

    @Override
    public String version() {
        return Version.getVersion();
    }

    @Override
    public void start(Map<String, String> props) {
        String topic = props.get(AsteriskAmiConnectorConfig.TOPIC_NAME);
        String astIpAddress = props.get(AsteriskAmiConnectorConfig.AST_IP_ADDRESS);
        int astPort = Integer.valueOf(props.get(AsteriskAmiConnectorConfig.AST_IP_PORT));
        String astUsername = props.get(AsteriskAmiConnectorConfig.AST_USERNAME);
        String astPassword = props.get(AsteriskAmiConnectorConfig.AST_PASSWORD);
        String astEvent = props.get(AsteriskAmiConnectorConfig.AST_EVENTS);
        this.batchSize = Integer.valueOf(props.get(AsteriskAmiConnectorConfig.BATCH_SIZE));
        this.managerConnection = getManagerConnection(astIpAddress, astPort, astUsername, astPassword);
        this.queueRecords = new ConcurrentLinkedQueue<>();

        ManagerEventListener eventListener = new AsteriskAmiCdrEventsProducer(queueRecords, topic);
        this.managerConnection.addEventListener(eventListener);

        managerLogin(astEvent);

    }

    private void managerLogin(String astEvent) {
        try {
            this.managerConnection.login(astEvent);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthenticationFailedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }


    public List<SourceRecord> poll() {
        if (queueRecords.size() > 0)
            return getRecords();

        return new ArrayList<>();
    }

    @Override
    public void stop() {
        this.managerConnection.logoff();
    }

    private ManagerConnection getManagerConnection(String ipAddress,
                                                   int port,
                                                   String username,
                                                   String password) {
        ManagerConnectionFactory factory = new ManagerConnectionFactory(ipAddress, port, username, password);
        this.managerConnection = factory.createManagerConnection();

        return this.managerConnection;
    }

    private List<SourceRecord> getRecords() {
        ArrayList<SourceRecord> records = new ArrayList<>();
        for (int i = 0; i <= batchSize; i++) {
            records.add(queueRecords.poll());
            logger.debug(" queueRecords.poll() " + queueRecords.size());
        }

        return records;
    }
}
