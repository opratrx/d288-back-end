package com.aaron.d288.dao;

import com.aaron.d288.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface CountryRepository extends JpaRepository<Country, Long> {
}
