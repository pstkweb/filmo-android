package com.sixfingers.filmo.dvdfrapi.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "errors")
public class Errors {
    @ElementList(inline = true, required = false)
    private List<Error> errors;

    public Errors() {}

    public List<Error> getErrors() {
        return errors;
    }
}

@Root(name = "error")
class Error {
    @Attribute(name = "type")
    private String type;
    @Element(name = "code")
    private String code;
    @Element(name = "message")
    private String message;

    public Error() {}

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
