package uk.ac.aston.daviesg8.snowdome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.aston.daviesg8.snowdome.model.entity.Client;

import java.util.Optional;

/**
 * This repository is used to interact with the 'clients' table
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByUsernameAndPassword(String username, String password);

    Optional<Client> findByUsername(String username);

}
