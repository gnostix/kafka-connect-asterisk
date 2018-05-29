package gr.gnostix.kafka.connect.asterisk.config;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by gnostix on 17/5/2018.
 */
public class AsteriskAmiConnectorConfig extends AbstractConfig {

    public AsteriskAmiConnectorConfig(Map<String, String> props){
        super(CONFIG_DEF, props);
    }

    private static final Logger logger = LoggerFactory.getLogger(AsteriskAmiConnectorConfig.class);
    public static final String TOPIC_NAME = "asterisk.topic";
    public static final String AST_IP_ADDRESS = "asterisk.ipaddress";
    public static final String AST_IP_PORT = "asterisk.port";
    public static final String AST_USERNAME = "asterisk.username";
    public static final String AST_PASSWORD = "asterisk.password";
    public static final String AST_EVENTS = "asterisk.events";
    public static final String BATCH_SIZE = "batch.size";

    private static final String DEFAULT_TOPIC_NAME = "asterisk";
    private static final String DEFAULT_AST_HOST = "127.0.0.1";
    private static final int DEFAULT_AST_PORT = 5038;
    private static final String DEFAULT_AST_USERNAME = "kafka-connect";
    private static final String DEFAULT_AST_PASSWORD = "mysecret";
    private static final String DEFAULT_AST_EVENTS = "on";
    private static final int DEFAULT_BATCH_SIZE = 100;

    private static ConfigDef baseConfigDef() {
        return new ConfigDef()
                .define(AST_IP_ADDRESS, ConfigDef.Type.STRING, DEFAULT_AST_HOST, ConfigDef.Importance.HIGH, "Asterisk server ip address")
                .define(AST_IP_PORT, ConfigDef.Type.INT, DEFAULT_AST_PORT, ConfigDef.Importance.LOW, "Asterisk server port or get the default 5038")
                .define(AST_USERNAME, ConfigDef.Type.STRING, DEFAULT_AST_USERNAME, ConfigDef.Importance.HIGH, "Asterisk server AMI username")
                .define(AST_PASSWORD, ConfigDef.Type.STRING, DEFAULT_AST_PASSWORD, ConfigDef.Importance.HIGH, "Asterisk server AMI password")
                .define(AST_EVENTS, ConfigDef.Type.STRING, DEFAULT_AST_EVENTS, ConfigDef.Importance.HIGH, "Asterisk server AMI event to source")
                .define(TOPIC_NAME, ConfigDef.Type.STRING, DEFAULT_TOPIC_NAME, ConfigDef.Importance.HIGH, "Kafka topic to push the data")
                .define(BATCH_SIZE, ConfigDef.Type.INT, DEFAULT_BATCH_SIZE, ConfigDef.Importance.HIGH, "Kafka batch size to push to topic");
    }

    public static final ConfigDef CONFIG_DEF = baseConfigDef();


    public boolean validateNumber(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
