package gr.gnostix.kafka.connect.asterisk.config;

import gr.gnostix.kafka.connect.asterisk.source.AsteriskAmiSourceConnector;
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
    public static String TOPIC_NAME = "asterisk.topic";
    public static String AST_IP_ADDRESS = "asterisk.ipaddress";
    public static String AST_USERNAME = "asterisk.username";
    public static String AST_PASSWORD = "asterisk.password";
    public static String AST_EVENTS = "asterisk.events";
    public static String BATCH_SIZE = "batch.size";

    private static ConfigDef baseConfigDef() {
        return new ConfigDef()
                .define(AST_IP_ADDRESS, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Asterisk server ip address")
                .define(AST_USERNAME, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Asterisk server AMI username")
                .define(AST_PASSWORD, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Asterisk server AMI password")
                .define(AST_EVENTS, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Asterisk server AMI event to source")
                .define(TOPIC_NAME, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Kafka topic to push the data")
                .define(BATCH_SIZE, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Kafka batch size to push to topic");
    }

    public static final ConfigDef CONFIG_DEF = baseConfigDef();

    static void forceIpV4(String astIpAddress) {

    }

    private boolean validateNumber(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
