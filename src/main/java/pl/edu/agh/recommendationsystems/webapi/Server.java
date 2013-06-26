package pl.edu.agh.recommendationsystems.webapi;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Łukasz Macionczyk
 * Date: 24.06.13
 * Time: 02:33
 * To change this template use File | Settings | File Templates.
 */
public class Server {

    private final Logger LOGGER = LoggerFactory.getLogger(Server.class);


    private static ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    static final String BASE_URI = "http://localhost:9999/recommendationsystems/";

    public static void main(String[] args) {
        try {
            ResourceConfig rc = new PackagesResourceConfig("");
            rc.getProperties().put(
                    "com.sun.jersey.spi.container.ContainerRequestFilters",
                    "com.sun.jersey.api.container.filter.LoggingFilter;" +
                            "pl.edu.agh.recommendationsystems.webapi.auth.AuthFilter"
            );
            HttpServer server = HttpServerFactory.create(BASE_URI, rc);
            server.start();
            System.out.println("Press Enter to stop the server. ");
            System.in.read();
            server.stop(0);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
