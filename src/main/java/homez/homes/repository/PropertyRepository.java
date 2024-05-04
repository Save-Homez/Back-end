package homez.homes.repository;

import homez.homes.entity.Property;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    @Query(value = "SELECT * FROM property WHERE town = :town ORDER BY rand()", nativeQuery = true)
    List<Property> findPropertiesByTownOrderByRandom(String town, Pageable pageable);
}
