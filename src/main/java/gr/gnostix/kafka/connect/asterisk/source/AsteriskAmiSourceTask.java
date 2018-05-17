package gr.gnostix.kafka.connect.asterisk.source;

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
    ConcurrentLinkedQueue<SourceRecord> queueRecords;
    private ManagerConnection managerConnection;
    private String topic;
    private String astIpAddress;
    private String astUsername;
    private String astPassword;
    private String astEvent;
    private ManagerEventListener eventListener;
    private int batchSize;

    @Override
    public String version() {
        return null;
    }

    @Override
    public void start(Map<String, String> props) {
        this.topic = props.get(AsteriskAmiSourceConnector.TOPIC_NAME);
        this.astIpAddress = props.get(AsteriskAmiSourceConnector.AST_IP_ADDRESS);
        this.astUsername = props.get(AsteriskAmiSourceConnector.AST_USERNAME);
        this.astPassword = props.get(AsteriskAmiSourceConnector.AST_PASSWORD);
        this.astEvent = props.get(AsteriskAmiSourceConnector.AST_EVENTS);
        this.batchSize = Integer.valueOf(props.get(AsteriskAmiSourceConnector.BATCH_SIZE));
        this.managerConnection = getManagerConnection(astIpAddress, astUsername, astPassword);
        this.queueRecords = new ConcurrentLinkedQueue<>();

        this.eventListener = new AsteriskAmiCdrEventsProducer(queueRecords, topic);
        this.managerConnection.addEventListener(eventListener);

        managerLogin();

    }

    private void managerLogin() {
        try {
            this.managerConnection.login("on");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthenticationFailedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }


    public List<SourceRecord> poll() throws InterruptedException {
        if (queueRecords.size() > 0)
            return getRecords();

        return null;
    }

    @Override
    public void stop() {
        this.managerConnection.logoff();
    }

    private ManagerConnection getManagerConnection(String ipAddress,
                                                   String username,
                                                   String password) {
        ManagerConnectionFactory factory = new ManagerConnectionFactory(ipAddress, username, password);
        this.managerConnection = factory.createManagerConnection();

        return this.managerConnection;
    }

    public List<SourceRecord> getRecords() {
        ArrayList<SourceRecord> records = new ArrayList<>();
        for (int i = 0; i <= batchSize; i++) {
            records.add(queueRecords.poll());
            logger.info(" queueRecords.poll() " + queueRecords.size());
        }

        return records;
    }
}
