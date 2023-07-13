package cz.pwc.borders.service;

import cz.pwc.borders.model.Country;
import cz.pwc.borders.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    private final CountryRepository repository;
    private final Graph<Country> graph;

    public CountryService(CountryRepository repository, Graph<Country> graph) {
        this.repository = repository;
        this.graph = graph;
    }

    public void save(Country country) {
        repository.save(country);
        graph.addNode(country);
    }

    public Country getByCca3(String cca3) {
        return repository.getCountryByCca3(cca3);
    }

    public void addBorders(Country c1, Country c2) {
        graph.addBorder(c1, c2);
    }

    public List<String> getRoute(String originCca3, String destinationCca3) {
        Country origin = repository.getCountryByCca3(originCca3);
        Country destination = repository.getCountryByCca3(destinationCca3);
        List<Country> path = graph.getOptimalPath(origin, destination);

        return path.stream().map(Country::getCca3).toList();
    }

}
