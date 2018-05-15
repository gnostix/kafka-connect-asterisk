package gr.gnostix.kafka.connect.asterisk.source;

import org.apache.kafka.connect.source.SourceRecord;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.event.ManagerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by gnostix on 15/5/2018.
 */
public class AsteriskAmiCdrEvents implements ManagerEventListener {
    private List<SourceRecord> records;
    private int batchSize;

    public AsteriskAmiCdrEvents(List<SourceRecord> records, int batchSize){
        this.records = records;
        this.batchSize = batchSize;
    }

    private static final Logger logger = LoggerFactory.getLogger(AsteriskAmiSourceConnector.class);


    public void onManagerEvent(ManagerEvent event) {

        // just print received events
        logger.info(event.toString());

    }

}
