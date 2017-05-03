package com.adobe.cq;


public interface SampleService {

    public String helloWorld();

    public String getName(String path) throws org.apache.sling.api.resource.LoginException;
}
