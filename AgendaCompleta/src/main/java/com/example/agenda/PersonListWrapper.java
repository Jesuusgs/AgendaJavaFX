package com.example.agenda;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="persons")
public class PersonListWrapper {
    private List<Persona> persons;

    @XmlElement(name="persons")

    //obtener los datos de las personas
    public List<Persona> getPerson(){
        return persons;
    }

    //pasar los datos de person a XML
    public void setPerson(List<Persona> persons){
        this.persons = persons;
    }
}
