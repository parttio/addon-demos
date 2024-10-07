package org.example.data.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

// Needed to handle circular references at least to some level
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class ComplexPerson extends Person {

    private Double doubleValue;
    private ComplexPerson supervisor;

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public ComplexPerson getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(ComplexPerson supervisor) {
        this.supervisor = supervisor;
    }
}
