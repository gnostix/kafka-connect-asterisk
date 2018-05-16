package gr.gnostix.kafka.connect.asterisk.source;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.asteriskjava.manager.ManagerConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gnostix on 15/5/2018.
 */
public class AsteriskAmiSourceConnector extends SourceConnector {
    private static final Logger logger = LoggerFactory.getLogger(AsteriskAmiSourceConnector.class);
    public static String TOPIC_NAME = "asterisk.topic";
    public static String AST_IP_ADDRESS = "ip.address";
    public static String AST_USERNAME = "username";
    public static String AST_PASSWORD = "password";
    public static String AST_EVENT = "asterisk.event";
    public static String BATCH_SIZE = "batch.size";

    private static final ConfigDef CONFIG_DEF = new ConfigDef()
            .define(AST_IP_ADDRESS, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Asterisk server ip address")
            .define(AST_USERNAME, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Asterisk server AMI username")
            .define(AST_PASSWORD, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Asterisk server AMI password")
            .define(AST_EVENT, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Asterisk server AMI event to source")
            .define(TOPIC_NAME, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Kafka topic to push the data")
            .define(BATCH_SIZE, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Kafka batch size to push to topic");
    private String topic;
    private String astIpAddress;
    private String astUsername;
    private String astPassword;
    private String astEvent;
    private String batchSize;
    private ManagerConnection managerConnection;

    static void forceIpV4(String astIpAddress) {

    }


    @Override
    public String version() {
        return null;
    }

    @Override
    public void start(Map<String, String> props) {
        topic = props.get(TOPIC_NAME);
        astIpAddress = props.get(AST_IP_ADDRESS);
        astUsername = props.get(AST_USERNAME);
//        astUsername = forceIpV4(props.get(AST_USERNAME));
        astPassword = props.get(AST_PASSWORD);
        astEvent = props.get(AST_PASSWORD);
        batchSize = props.get(BATCH_SIZE);
    }

    @Override
    public Class<? extends Task> taskClass() {
        return AsteriskAmiSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        ArrayList<Map<String, String>> configs = new ArrayList<>();

        Map<String, String> config = new HashMap<>();
        config.put(TOPIC_NAME, topic);
        config.put(AST_IP_ADDRESS, astIpAddress);
        config.put(AST_USERNAME, astUsername);
        config.put(AST_PASSWORD, astPassword);
        config.put(AST_EVENT, astEvent);
        config.put(BATCH_SIZE, batchSize);

        configs.add(config);

        return configs;
    }

    @Override
    public void stop() {

    }

    @Override
    public ConfigDef config() {
        return CONFIG_DEF;
    }
}
