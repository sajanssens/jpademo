package com.example.demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data @SuperBuilder @NoArgsConstructor @AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Person { // POJO

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name = null;
    private int age = 0;

    @ManyToOne/*(cascade = PERSIST)*/
    private Team myTeam;

    public void setMyTeam(Team aTeam) {
        this.myTeam = aTeam;
        aTeam.addMember(this);
    }

    @Data @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @Entity
    public static class Sales extends Person {
        private int commission;
    }

    @Data @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @Entity
    public static class Programmer extends Person {
        private String language;
    }
}



