package com.retail.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
