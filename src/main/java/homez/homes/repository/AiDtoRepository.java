package homez.homes.repository;

import homez.homes.entity.AiDto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiDtoRepository extends JpaRepository<AiDto, Long>, AiDtoRepositoryCustom {
    Optional<AiDto> findByStation(String station);
}
