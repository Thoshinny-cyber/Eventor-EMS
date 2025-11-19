package com.restapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Discount implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Date fromDate;

    @Column(nullable = false)
    private Date toDate;

    private int discount;
}
