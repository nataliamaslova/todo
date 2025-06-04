package api.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ReqSpecs {
    public RequestSpecification unauthSpec() {
        var reqSpecBuilder = new RequestSpecBuilder();
        reqSpecBuilder.setContentType(ContentType.JSON);
        return reqSpecBuilder.build();
    }
}
