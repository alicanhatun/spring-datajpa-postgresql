package com.alican.postgres.service.impl;

import com.alican.postgres.dto.PersonDto;
import com.alican.postgres.entity.Address;
import com.alican.postgres.entity.AddressType;
import com.alican.postgres.entity.Person;
import com.alican.postgres.repo.AddressRepository;
import com.alican.postgres.repo.PersonRepository;
import com.alican.postgres.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<PersonDto> findAll() {
        List<Person> personList = personRepository.findAll();
        List<PersonDto> personDtoList = new ArrayList<>(personList.size());

        personList.forEach(it -> {
            PersonDto personDto = new PersonDto();
            personDto.setId(it.getId());
            personDto.setName(it.getName());
            personDto.setSurname(it.getSurname());
            personDto.setAddresses(
                    it.getAddresses() != null ?
                            it.getAddresses().stream().map(Address::getAddress).collect(Collectors.toList())
                            : null);
            personDtoList.add(personDto);
        });
        return personDtoList;
    }

    @Override
    @Transactional
    public PersonDto save(PersonDto personDto) {
        Person person = new Person();
        person.setName(personDto.getName());
        person.setSurname(personDto.getSurname());
        final  Person personFromDB = personRepository.save(person);

        List<Address> addressList = new ArrayList<>();
        personDto.getAddresses().forEach(item -> {
            Address address = new Address();
            address.setAddress(item);
            address.setAddressType(AddressType.HOME);
            address.setPerson(personFromDB);
            addressList.add(address);
        });
        addressRepository.saveAll(addressList);
        personDto.setId(personFromDB.getId());
        return personDto;
    }
}
