package com.dimanddim.erentbackend.api.v1.repositories;

import com.dimanddim.erentbackend.api.v1.entities.JwtTokensWhiteList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtTokensWhiteListRepository extends CrudRepository<JwtTokensWhiteList, String> {

}