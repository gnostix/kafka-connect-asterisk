package gr.gnostix.kafka.connect.asterisk.source;

import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.asteriskjava.manager.*;
import org.asteriskjava.manager.event.CdrEvent;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by gnostix on 15/5/2018.
 */
public class AsteriskAmiSourceTask extends SourceTask {

    private ManagerConnection managerConnection;
    private String topic;
    private String astIpAddress;
    private String astUsername;
    private String astPassword;
    private String astEvent;
    private ManagerEventListener eventListener;


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

        this.eventListener = new AsteriskAmiCdrEvents();

        this.managerConnection.addEventListener(eventListener);
        this.managerConnection = getManagerConnection(astIpAddress, astUsername, astPassword);

        managerLogin();

    }

    private void managerLogin() {
        try {
            this.managerConnection.login("log");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthenticationFailedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<SourceRecord> poll() throws InterruptedException {
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
}
