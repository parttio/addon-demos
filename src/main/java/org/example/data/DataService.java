package org.example.data;

import jakarta.annotation.PostConstruct;
import org.apache.commons.beanutils.BeanComparator;
import org.example.data.domain.Group;
import org.example.data.domain.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.apache.commons.lang3.BooleanUtils.forEach;

@Service
public class DataService {

    final Random r = new Random(0);

    final List<Group> groups = Arrays.asList(new Group("Athletes"), new Group("Nerds"), new Group("Collegues"),
            new Group("Students"), new Group("Hunting club members"));

    private List<Person> pagedBase;

    @PostConstruct
    void init() {
        if (pagedBase == null) {
            pagedBase = getListOfPersons(1000);
        }
    }

    public List<Person> findPersons(int start, int pageSize, String sortProperty, boolean asc) {
        System.err.println("findAll " + start + " " + pageSize + " sort " + sortProperty + " asc:" + asc );
        int end = (int) (start + pageSize);
        if (end > pagedBase.size()) {
            end = pagedBase.size();
        }
        Collections.sort(pagedBase, new BeanComparator<Person>(sortProperty));

        if(!asc) {
            Collections.reverse(pagedBase);
        }
        return pagedBase.subList((int) start, end);

    }

    public List<Person> findPersons(String firstNameFilter, int start, int pageSize, String sortProperty, boolean asc) {
        System.err.println("findAll " + firstNameFilter + " "  + start + " " + pageSize + " sort " + sortProperty + " asc:" + asc );

        var base = pagedBase.stream().filter(p -> p.getFirstName().toLowerCase().contains(firstNameFilter.toLowerCase())).toList();
        base = new ArrayList<>(base);
        int end = (int) (start + pageSize);
        if (end > base.size()) {
            end = base.size();
        }
        Collections.sort(base, new BeanComparator<Person>(sortProperty));

        if(!asc) {
            Collections.reverse(base);
        }
        return base.subList((int) start, end);

    }

    public List<Person> findPersons(int start, int pageSize) {
        System.err.println("findAll " + start + " " + pageSize);
        int end = (int) (start + pageSize);
        if (end > pagedBase.size()) {
            end = pagedBase.size();
        }
        return pagedBase.subList((int) start, end);
    }


    public List<Person> getListOfPersons(int total) {
        List<Person> l = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            Person p = new Person();
            p.setId(i + 1);
            p.setFirstName("First" + i);
            p.setLastName("Lastname" + i);
            p.setAge(r.nextInt(100));
            l.add(p);
        }
        return l;
    }

}
