package gr.gnostix.kafka.connect.asterisk.config;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by gnostix on 21/05/2018.
 */
public class AsteriskAmiConnectorConfigTest {

    private AsteriskAmiConnectorConfig config;

    @Before
    public void initialize(){
        this.config = new AsteriskAmiConnectorConfig(new HashMap<String, String>());

    }

    @Test
    public void shouldValidateIntegerWithTrueForNumber() {
        String number = "25";
        assertTrue(this.config.validateNumber(number));
    }

    @Test
    public void shouldValidateIntegerWithFalseForString() {
        String number = "aa25";
        assertFalse("the number is not an Int " + number, this.config.validateNumber(number));
    }

    @Test
    public void shouldValidateIntegerWithFalseForEmptyString() {
        String number = "";
        assertFalse("String is empty or null " , this.config.validateNumber(number));
    }

    @Test
    public void validateDeafaultPortNumber(){
        assertEquals(this.config.getInt("asterisk.port").longValue(), 5038L);
    }

}