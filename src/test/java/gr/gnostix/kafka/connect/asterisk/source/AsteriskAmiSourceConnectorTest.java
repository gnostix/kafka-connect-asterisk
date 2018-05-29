package gr.gnostix.kafka.connect.asterisk.source;

import gr.gnostix.kafka.connect.asterisk.config.AsteriskAmiConnectorConfig;
import org.apache.kafka.connect.connector.ConnectorContext;
import org.junit.After;
import org.junit.Before;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.*;

/**
 * Created by gnostix on 25/05/2018.
 */
public class AsteriskAmiSourceConnectorTest {

    private static final String SINGLE_TOPIC = "asterisk";
    private static final String MULTIPLE_TOPICS = "asterisk.cdr,asterisk.all";
    private static final String DEFAULT_AST_PORT = "5038";

    private AsteriskAmiSourceConnector connector;
    private ConnectorContext ctx;
    private Map<String, String> sourceProperties;
    private AsteriskAmiConnectorConfig config;

    @Before
    public void setup() {
        config = new AsteriskAmiConnectorConfig(new HashMap<String, String>());
        connector = new AsteriskAmiSourceConnector();
        ctx = createMock(ConnectorContext.class);
        connector.initialize(ctx);

        sourceProperties = new HashMap<>();
        sourceProperties.put(AsteriskAmiConnectorConfig.TOPIC_NAME, SINGLE_TOPIC);
        sourceProperties.put(AsteriskAmiConnectorConfig.AST_IP_PORT, DEFAULT_AST_PORT);

        connector.start(sourceProperties);
    }

    @Test
    public void testTaskClass(){
        assertEquals(connector.taskClass(), AsteriskAmiSourceTask.class);
    }

    @Test
    public void testSourceTasks(){
        List<Map<String, String>> config = connector.taskConfigs(1);
        assertEquals(config.size(), 1);

        // check topic name
        assertEquals(SINGLE_TOPIC, config.get(0).get(AsteriskAmiConnectorConfig.TOPIC_NAME));

        // check default port
        assertEquals(DEFAULT_AST_PORT, config.get(0).get(AsteriskAmiConnectorConfig.AST_IP_PORT));
    }


    @After
    public void cleanResources(){
        connector.stop();
    }

}