package pl.edu.agh.recommendationsystems.webapi.auth;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.agh.recommendationsystems.persistence.Person;
import pl.edu.agh.recommendationsystems.persistence.repository.PersonRepository;
import pl.edu.agh.recommendationsystems.webapi.Server;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Jersey HTTP Basic Auth filter
 * @author Deisss (LGPLv3)
 */
public class AuthFilter implements ContainerRequestFilter {

    @Autowired
    PersonRepository personRepository;

    public AuthFilter() {
        Server.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
    }

    /**
     * Apply the filter : check input request, validate or not with user auth
     * @param containerRequest The request from Tomcat server
     */
    @Override
    public ContainerRequest filter(ContainerRequest containerRequest) throws WebApplicationException {
        //GET, POST, PUT, DELETE, ...
        String method = containerRequest.getMethod();
        // myresource/get/56bCA for example
        String path = containerRequest.getPath(true);

        //We do allow wadl to be retrieve
        if(method.equals("GET") && (path.equals("application.wadl") || path.equals("application.wadl/xsd0.xsd"))) {
            return containerRequest;
        }

        //Get the authentification passed in HTTP headers parameters
        String auth = containerRequest.getHeaderValue("authorization");

        //If the user does not have the right (does not provide any HTTP Basic Auth)
        if(auth == null){
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        //lap : loginAndPassword
        String[] lap = Base64Decoder.decode(auth);

        //If login or password fail
        if(lap == null || lap.length != 2){
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        //DO YOUR DATABASE CHECK HERE (replace that line behind)...
        Person person =  personRepository.findPersonByUsernameAndPassword(lap[0], lap[1]);

        //Our system refuse login and password
        if(person == null){
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        //HERE YOU SHOULD ADD PARAMETER TO REQUEST, TO REMEMBER USER ON YOUR REST SERVICE...

        containerRequest.getRequestHeaders().add("personId", String.valueOf(person.getId()));

        return containerRequest;
    }
}
