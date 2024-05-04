package homez.homes.repository;

import homez.homes.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {
    StationTownOnly findByName(String name);
}
