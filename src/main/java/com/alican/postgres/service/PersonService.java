package com.alican.postgres.service;

import com.alican.postgres.dto.PersonDto;

import java.util.List;

public interface PersonService {

    List<PersonDto> findAll();

    PersonDto save(PersonDto personDto);
}
