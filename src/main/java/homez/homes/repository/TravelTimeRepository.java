package homez.homes.repository;

import homez.homes.entity.TravelTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TravelTimeRepository extends JpaRepository<TravelTime, Long> {
    @Query("SELECT t FROM TravelTime t WHERE t.origin = :origin AND t.destination IN :stations")
    List<TravelTime> findByOriginAndDestinations(@Param("origin") String origin,
                                                 @Param("stations") List<String> stations);
}
