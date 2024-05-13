package homez.homes.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import homez.homes.entity.QStation;
import homez.homes.entity.Station;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StationRepositoryImpl implements StationRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Station> findOneByName(String name) {
        QStation station = QStation.station;

        Station result = queryFactory.select(station)
                .from(station)
                .where(station.name.eq(name))
                .fetchFirst();
        return Optional.ofNullable(result);
    }
}
