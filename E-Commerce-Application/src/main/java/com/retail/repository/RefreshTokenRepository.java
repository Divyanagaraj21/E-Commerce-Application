package com.retail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.retail.model.RefreshToken;
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

	boolean existsByTokenAndIsBlocked(String rt, boolean b);

}
