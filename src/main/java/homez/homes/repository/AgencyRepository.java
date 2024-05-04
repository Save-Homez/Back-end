package homez.homes.repository;

import homez.homes.entity.Agency;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AgencyRepository extends JpaRepository<Agency, Long>{
    @Query(value = "SELECT * FROM agency WHERE town = :town ORDER BY rand()", nativeQuery = true)
    List<Agency> findAgenciesByTownOrderByRandom(String town);
}
