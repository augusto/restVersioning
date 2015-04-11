package com.augusto.test.web.controller;


import com.augusto.test.spring.version.VersionedResource;
import com.augusto.test.web.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@VersionedResource(media = "application/vnd.app.resource")
public class PeopleController {
    private Logger logger = LoggerFactory.getLogger(PeopleController.class);

    @RequestMapping(value = {"/people"}, method = RequestMethod.GET)
    @VersionedResource(from = "1.0", to = "1.0")
    @ResponseBody
    public Person getPerson_v1() {
        logger.info("called /people with v1.0");

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAge(27);

        return person;
    }

    @RequestMapping(value = {"/people"}, method = RequestMethod.GET)
    @VersionedResource(from = "2.0", to = "2.0")
    @ResponseBody
    public Person getPerson_v2() {
        logger.info("called /people with v2.0");

        Person person = new Person();
        person.setFirstName("Carl");
        person.setLastName("Marx");
        person.setAge(55);

        return person;
    }

    @RequestMapping(value = {"/people"}, method = RequestMethod.GET)
    @VersionedResource(from = "2.1")
    @ResponseBody
    public Person getPerson_latest() {
        logger.info("called /people latest");

        Person person = new Person();
        person.setFirstName("Douglas");
        person.setLastName("Adams");
        person.setAge(42);

        return person;
    }

}
