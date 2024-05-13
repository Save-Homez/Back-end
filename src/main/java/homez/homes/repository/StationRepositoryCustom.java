package homez.homes.repository;

import homez.homes.entity.Station;
import java.util.Optional;

public interface StationRepositoryCustom {
    Optional<Station> findOneByName(String name);
}
