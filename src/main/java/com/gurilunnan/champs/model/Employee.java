package com.gurilunnan.champs.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author Glenn Bech
 */
@XmlRootElement
public class Employee {

    private String name;



    public Employee(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                '}';
    }
}