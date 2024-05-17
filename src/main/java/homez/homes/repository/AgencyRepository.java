package homez.homes.repository;

import homez.homes.entity.Agency;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
    @Query(value = "SELECT * FROM agency WHERE LEFT(town, 2) = LEFT(:town,2) ORDER BY rand()", nativeQuery = true)
    List<Agency> findByTownOrderByRandom(String town, Pageable pageable);
    @Query("SELECT a FROM Agency a WHERE a.address LIKE %:addressGu%")
    List<Agency> findByAddressLike(String addressGu);
}
