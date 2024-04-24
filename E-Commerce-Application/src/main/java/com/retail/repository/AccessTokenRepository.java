package com.retail.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.retail.model.AccessToken;
@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Integer> {


	boolean existsByTokenAndIsBlocked(String at, boolean b);

}
