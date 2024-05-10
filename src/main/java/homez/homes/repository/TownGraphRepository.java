package homez.homes.repository;

import homez.homes.entity.TownGraph;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TownGraphRepository extends JpaRepository<TownGraph, Long> {
    Optional<TownGraph> findByTown(String town);
}
