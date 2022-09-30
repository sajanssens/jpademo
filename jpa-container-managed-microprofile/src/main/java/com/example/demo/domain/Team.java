package com.example.demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Entity
public class Team {

    @Id @GeneratedValue
    private long id;

    private String name;

    @OneToMany(mappedBy = "myTeam")
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private Set<Person> members = new HashSet<>();

    public void addMember(Person person) {
        this.members.add(person);
    }
}
