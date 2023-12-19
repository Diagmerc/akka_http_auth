package ru.lozovoi;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.model.headers.BasicHttpCredentials;
import akka.http.javadsl.testkit.JUnitRouteTest;
import akka.http.javadsl.testkit.TestRoute;
import org.junit.Test;

public class UserServerTest extends JUnitRouteTest {
    ActorSystem system = ActorSystem.create("userServer");
    ActorRef userActor = system.actorOf(UserActor.props(), "userActor");
    TestRoute appRoute = testRoute(new UserServer(userActor).routes());

    @Test
    public void testLogin() {

        String user = "{\"name\": \"Vasya\", \"email\": \"vasya@mail.ru\", \"password\": \"123\"}";
        HttpRequest register = HttpRequest.POST("/register")
                .withEntity(ContentTypes.APPLICATION_JSON, user);
        HttpRequest login = HttpRequest.POST("/login")
                .withEntity(ContentTypes.APPLICATION_JSON, user);

        appRoute.run(register)
                .assertStatusCode(StatusCodes.OK);
        appRoute.run(login)
                .assertStatusCode(StatusCodes.OK);
    }

    @Test
    public void testNotRegisteredLogin() {

        String user = "{\"email\": \"vasy@mail.ru\", \"password\": \"123\"}";
        HttpRequest login = HttpRequest.POST("/login")
                .withEntity(ContentTypes.APPLICATION_JSON, user);

        appRoute.run(login)
                .assertStatusCode(StatusCodes.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void testGetLoginUser() {
        String user = "{\"email\": \"email1@mail.ru\", \"password\": \"pass\"}";
        BasicHttpCredentials pass = BasicHttpCredentials.createBasicHttpCredentials("email1@mail.ru", "pass");
        HttpRequest login = HttpRequest.POST("/login")
                .withEntity(ContentTypes.APPLICATION_JSON, user);

        HttpRequest me = HttpRequest.GET("/me").addCredentials(pass);

        appRoute.run(login)
                .assertStatusCode(StatusCodes.OK);

        appRoute.run(me)
                .assertStatusCode(StatusCodes.OK);
    }

    @Test
    public void testGetNotLoginUser() {

        String user = "{\"email\": \"email1@mail.ru\", \"password\": \"pass\"}";
        HttpRequest me = HttpRequest.POST("/me")
                .withEntity(ContentTypes.APPLICATION_JSON, user);

        appRoute.run(me)
                .assertStatusCode(StatusCodes.UNAUTHORIZED);
    }


}