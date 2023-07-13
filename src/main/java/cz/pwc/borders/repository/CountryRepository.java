package cz.pwc.borders.repository;

import cz.pwc.borders.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {

    Country getCountryByCca3(String cca3);

}
