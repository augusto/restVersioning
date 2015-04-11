#Spring REST versioning extension (POC)


This is a Spring extension that allows routing to different methods depending on the version of the resource that is
requested. This code is based on the suggestions from a Stack Overflow question I asked - [How to manage REST API versioning with
spring?](http://stackoverflow.com/questions/20198275/how-to-manage-rest-api-versioning-with-spring ).

##Approaches to manage versions
There are 2 main approaches to manage versions. One is to make the version explicit as part of the URL
(e.g. http://host.com/1.1/resource), but this goes against the purist view that a resource identifier, should be the
same regardless of the representation. The more *correct* approach is to use the Accept header to differentiate the
representation of the resource (version and format), for example calling http://host.com/resource with an Accept header
with the value application/vnd.app.resource-v1+json. The header mentions both the version and format expected.

Another aspect of versioning is whether to version each individual endpoint with a different version, or to version the
whole API (similar to what is done in programming APIs, in which when a new version is release, not all classes have 
changed).

## Code

Here's an example of how a controller looks like with this extension

```java
@Controller
@VersionedResource(media = "application/vnd.app.resource")
public class TestController {

    @RequestMapping(value = {"/resource"}, method = RequestMethod.GET)
    @VersionedResource(from = "1.0", to = "1.0")
    @ResponseBody
    public Resource getResource_v1() {
        return new Resource("1.0");
    }

    @RequestMapping(value = {"/resource"}, method = RequestMethod.GET)
    @VersionedResource(from = "2.0")
    @ResponseBody
    public Resource getResource_v2_onwards() {
        return new Resource("2.0");
    }
}
```

There are 2 main classes that used to extend spring `CustomRequestMappingHandlerMapping` and 
`VersionedResourceRequestCondition`. `CustomRequestMappingHandlerMapping` is the entry point of this extension and 
allows to to extend the default `RequestMappingHandlerMapping` in order to add a custom `RequestCondition`. 
`VersionedResourceRequestCondition` is responsible for figuring out if a controller method is a candidate to be invoked.


#Drawbacks of this implementation
1. Given that we need a custom `RequestMappingHandlerMapping`, instantiating it is non-trivial (see the 
`WebConfiguration` class) and there's a danger that this class will change in a follow up version of spring and might 
cause unexpected behaviour.
2. This implementation doesn't check for a version upper boundary in the `@VersionedResource` and also routes higher,
non-existent versions to methods without a version upper boundary 
(see `TestControllerTest.shouldReturnUnboundedVersionForCallToVersion32`).