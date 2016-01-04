package net.steppschuh.stockbrowser.shutterstock;

import org.junit.Test;

import java.security.InvalidParameterException;

import static org.junit.Assert.*;

public class ShutterStockApiTest {

    @Test
    public void testHttpClient_validatesCreadentials() throws Exception {
        try {
            ShutterStockApi.createHttpClient(null, null);
            fail("HttpClient created with invalid credentials");
        } catch (InvalidParameterException ex) {
        }
    }

}
