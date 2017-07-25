package cn.com.zdht.quiz.domain.entity;

import cn.com.zdht.quiz.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author 袁臻
 * 7/25/17
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "users", indexes = {@Index(name = "user_name_index", columnList = "user_name")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "user_name", unique = true, nullable = false)
    private String userName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Embedded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_code", nullable = false)
    private City city;

    /**
     * 空构造方法，必需
     */
    public User() {
    }

    /**
     * 根据给定的UserDTO和City对象构造用户对象
     * @param userDTO 一个UserDTO对象
     * @param city 一个City对象
     */
    public User(final UserDTO userDTO, final City city) {
        this.userName = userDTO.getUserName();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
        this.city = city;
    }
}

/*
CREATE TABLE users(id integer PRIMARY KEY, user_name text UNIQUE NOT NULL,email text NOT NULL, password text NOT NULL, city_code integer NOT NULL);
 */