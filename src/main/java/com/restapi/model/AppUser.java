package com.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "users") // don't use User
public class AppUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 10)
    private long phone;

    @Column(nullable = false, length = 10)
    private String gender;

    private String address;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role roles;

    @Column(nullable = false, length = 100)
    private String password;

    private String confirmPassword;

    @OneToMany(mappedBy = "users")
    private List<Order> order;

    @OneToMany(mappedBy = "user")
    private List<Seat> seat;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user")
    private UserDP dp;

}
