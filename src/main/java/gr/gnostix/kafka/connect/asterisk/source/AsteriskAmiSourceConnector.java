package gr.gnostix.kafka.connect.asterisk.source;

import gr.gnostix.kafka.connect.asterisk.config.AsteriskAmiConnectorConfig;
import gr.gnostix.kafka.connect.asterisk.config.Version;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gnostix on 15/5/2018.
 */
public class AsteriskAmiSourceConnector extends SourceConnector {
    private static final Logger logger = LoggerFactory.getLogger(AsteriskAmiSourceConnector.class);

    private Map<String, String> configProperties;
    private AsteriskAmiConnectorConfig config;



    @Override
    public String version() {
        return Version.getVersion();
    }

    @Override
    public void start(Map<String, String> props) {
        configProperties = props;
        config = new AsteriskAmiConnectorConfig(configProperties);
    }

    @Override
    public Class<? extends Task> taskClass() {
        return AsteriskAmiSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        ArrayList<Map<String, String>> configs = new ArrayList<>();

        configs.add(configProperties);

        return configs;
    }

    @Override
    public void stop() {

    }

    @Override
    public ConfigDef config() {
        return AsteriskAmiConnectorConfig.CONFIG_DEF;
    }
}
