package homez.homes.repository;

import homez.homes.entity.TravelTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TravelTimeRepository extends JpaRepository<TravelTime, Long> {
    @Query("SELECT t FROM TravelTime t WHERE t.destination = :destination AND t.origin IN :origins")
    List<TravelTime> findByOriginAndDestinations(String destination, List<String> origins);
    List<TravelTime> findByDestination(String destination);
    Optional<TravelTime> findByOriginAndDestination(String origin, String destination);
}
