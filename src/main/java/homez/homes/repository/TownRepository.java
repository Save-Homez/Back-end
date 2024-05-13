package homez.homes.repository;

import homez.homes.entity.Town;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TownRepository extends JpaRepository<Town, Long> {
    Optional<Town> findByTown(String town);
}
