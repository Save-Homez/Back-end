package homez.homes.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import homez.homes.entity.QAiDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AiDtoRepositoryImpl implements AiDtoRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsStation(String station) {
        QAiDto aiDto = QAiDto.aiDto;

        return queryFactory.select(aiDto.station)
                .from(aiDto)
                .where(aiDto.station.eq(station))
                .fetchFirst() != null;
    }
}
