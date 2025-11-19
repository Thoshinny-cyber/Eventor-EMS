package com.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.StringJoiner;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 100)
    private String name;

    private String image;

    @OneToMany(mappedBy = "category")
    private List<Event> events;

    @Override
    public String toString() {

        return new StringJoiner(", ", Category.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("image='" + image + "'")
                .add("events=" + events)
                .toString();
    }
}