package com.example.appdiz.Models;

import javax.persistence.*;

@Entity
@Table(name="user_table")

    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "id")
        public Integer id;

        public String username;

        public String password;

        public String rePassword;

        public Integer getId() {
            return id;
        }

        public User(){};

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public void setId(Integer id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

