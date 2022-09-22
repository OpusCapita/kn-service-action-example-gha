package dev.kameshs;

import java.util.*;
import org.apache.log4j.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//import com.microsoft.azure.functions.annotation.*;
//import com.microsoft.azure.functions.*;

/**
 * Azure Functions with HTTP Trigger.
 */
@Path("/oc")
public class HttpMocking {

    static Logger logger = Logger.getLogger(HttpMocking.class.getName());

    private int overrideIntValue( int oldValue, String  queryValue ) {
        String query = queryValue;

        if( query == null ) { query = ""; }
        if( query.length()>0) {
            return Integer.parseInt( query );
        }
        return oldValue;
    }

    @GET
    @Path("/mocktest")
    @Produces(MediaType.TEXT_PLAIN)
    public String mocktest(
        @QueryParam("OC_MOCKING_LOGPOINTS") String OC_MOCKING_LOGPOINTS,
        @QueryParam("OC_MOCKING_WAITMILLIS") String OC_MOCKING_WAITMILLIS,
        @QueryParam("OC_MOCKING_PAYLOAD") String OC_MOCKING_PAYLOAD
         ) {

        String uuidAsString = "dummy1234";
        int logpoints = 40; //Integer.parseInt( System.getenv("OC_MOCKING_LOGPOINTS") );
        int waitmillis = 100; //Integer.parseInt( System.getenv("OC_MOCKING_WAITMILLIS") );
        int mockedPayloadSize =255; //= Integer.parseInt( System.getenv("OC_MOCKING_PAYLOAD") );

        logpoints = overrideIntValue( logpoints, OC_MOCKING_LOGPOINTS );
        waitmillis = overrideIntValue( waitmillis, OC_MOCKING_WAITMILLIS );
        mockedPayloadSize = overrideIntValue( mockedPayloadSize, OC_MOCKING_PAYLOAD );

        String dummypayload = "";

        for( int i=0; i<(mockedPayloadSize/50); i++) {
            dummypayload += ("12345678901234567890123456789012345678901234567890" + i);
        }

        logger.info("Java HTTP Mocking trigger processed a request started " + uuidAsString + "." );
        logger.info("logpoints=" + logpoints+ ", waitmillis="+waitmillis+", mockedPayloadSize="+mockedPayloadSize);

        for( int i=0; i<logpoints; i++) {
            logger.info("Mocking logpoint #" + i + " " + uuidAsString );
        }
        try {
            Thread.sleep( waitmillis );
        } catch (InterruptedException e) {
            logger.info("Java HTTP Mocking trigger wait crashed" );
        }
        logger.info("Java HTTP Mocking trigger processed a request ended. " + uuidAsString );

        return
            uuidAsString + "\n" +
            dummypayload.length() + "\n" +
            "START___" + dummypayload + "___END"
            ;

    }
}
