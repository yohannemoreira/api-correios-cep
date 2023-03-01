package buscadorcep.repository;

import buscadorcep.model.Address;
import buscadorcep.model.AddressStatus;
import org.springframework.data.repository.CrudRepository;

public interface AddressStatusRepository extends CrudRepository<AddressStatus, Integer> {
}
