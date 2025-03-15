package com.example.mongodemo.repositories;

import java.util.List;
import java.util.Optional;
import models.Zip;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ZipRepository extends MongoRepository<Zip, String> {

    List<Zip> findAll();

    Optional<Zip> findById(String id);

    Zip save(Zip zip);

    void deleteById(String id);
}
