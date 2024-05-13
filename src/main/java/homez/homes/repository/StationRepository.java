package homez.homes.repository;

import homez.homes.entity.Station;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<Station, Long>, StationRepositoryCustom {
    @Query(value = "SELECT * FROM station s WHERE ST_Distance_Sphere(point(:longitude, :latitude), s.coordinate) <= :radius", nativeQuery = true)
    List<Station> findWithinRadius(@Param("longitude") double longitude, @Param("latitude") double latitude,
                                   @Param("radius") int radius);
    Optional<List<Station>> findByTown(String town);
}
