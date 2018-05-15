package gr.gnostix.kafka.connect.asterisk.source;

import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.asteriskjava.manager.*;
import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.ManagerEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gnostix on 15/5/2018.
 */
public class AsteriskAmiSourceTask extends SourceTask implements ManagerEventListener {

    private ManagerConnection managerConnection;
    private String topic;
    private String astIpAddress;
    private String astUsername;
    private String astPassword;
    private String astEvent;
    private ManagerEventListener eventListener;
    private int batchSize = 1000;
    ArrayList<SourceRecord> records = null;

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
        this.astEvent = props.get(AsteriskAmiSourceConnector.AST_EVENT);


        this.managerConnection.addEventListener(eventListener);
        this.managerConnection = getManagerConnection(astIpAddress, astUsername, astPassword);

        managerLogin();

    }

    private void managerLogin() {
        try {
            this.managerConnection.login();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthenticationFailedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }


    public List<SourceRecord> poll() throws InterruptedException {
        return getRecords();
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

    @Override
    public void onManagerEvent(ManagerEvent event) {
        SourceRecord record = new SourceRecord(null, null, topic, null, null, null,
                null, event.getDateReceived().toString(), System.currentTimeMillis());
        records.add(record);

        if (records.size() >= batchSize) {
            try {
                poll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public synchronized List<SourceRecord> getRecords(){
        ArrayList<SourceRecord> recs = this.records;
        this.records = null;

        return recs;
    }
}
