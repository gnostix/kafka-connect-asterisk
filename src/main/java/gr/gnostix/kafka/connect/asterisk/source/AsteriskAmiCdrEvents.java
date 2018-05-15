package gr.gnostix.kafka.connect.asterisk.source;

import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.event.ManagerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gnostix on 15/5/2018.
 */
public class AsteriskAmiCdrEvents implements ManagerEventListener {
    private static final Logger logger = LoggerFactory.getLogger(AsteriskAmiSourceConnector.class);

    @Override
    public void onManagerEvent(ManagerEvent event) {
        // just print received events
        logger.info(event.toString());
    }
}
