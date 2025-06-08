package api.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.preemptive;

public class Specifications {
    private static RequestSpecBuilder reqBuilder() {
        var requestBuilder = new RequestSpecBuilder();
        requestBuilder.addFilter(new RequestLoggingFilter());
        requestBuilder.addFilter(new ResponseLoggingFilter());
        requestBuilder.setContentType(ContentType.JSON);
        requestBuilder.setAccept(ContentType.JSON);
        return requestBuilder;
    }

    public  static RequestSpecification unAuthSpec() {
        var requestBuilder = reqBuilder();
        return requestBuilder.build();
    }

    public static RequestSpecification authSpec() {
        var requestBuilder = reqBuilder();
        requestBuilder.setAuth(preemptive().basic("admin", "admin"));
        return requestBuilder.build();
    }

    public static RequestSpecification invalidAuthSpec() {
        var requestBuilder = reqBuilder();
        requestBuilder.setAuth(preemptive().basic("user", "user"));
        return requestBuilder.build();
    }
}
