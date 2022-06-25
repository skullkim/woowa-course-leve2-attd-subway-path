package wooteco.subway.domain;

import java.util.List;
import wooteco.subway.domain.discountpolicy.AgeDiscountPolicy;
import wooteco.subway.domain.line.Lines;

public class Fare {

    private static final int DEFAULT_FARE = 1250;
    private static final int ADDITIONAL_DISTANCE_PER_5KM = 10;
    private static final int ADDITIONAL_DISTANCE_PER_8KM = 51;
    private static final int DISTANCE_UNIT_UNDER_50 = 5;
    private static final int DISTANCE_UNIT_OVER_50 = 8;
    private static final int ADDITIONAL_AMOUNT = 100;

    private final Lines lines;
    private final AgeDiscountPolicy ageDiscountPolicy;

    public Fare(final Lines lines, final AgeDiscountPolicy ageDiscountPolicy) {
        this.lines = lines;
        this.ageDiscountPolicy = ageDiscountPolicy;
    }

    public int calculate(final int distance, final List<Station> stations) {
        final int extraLineFare = lines.calculateExtraLineFare(stations);
        int fare = DEFAULT_FARE + extraLineFare;
        if (ADDITIONAL_DISTANCE_PER_5KM <= distance && distance < ADDITIONAL_DISTANCE_PER_8KM) {
             fare += addExtraFare(distance, DISTANCE_UNIT_UNDER_50, ADDITIONAL_DISTANCE_PER_5KM);
        }
        if (ADDITIONAL_DISTANCE_PER_8KM <= distance) {
            fare = fare
                    + addExtraFare(ADDITIONAL_DISTANCE_PER_8KM - 1, DISTANCE_UNIT_UNDER_50, ADDITIONAL_DISTANCE_PER_5KM)
                    + addExtraFare(distance, DISTANCE_UNIT_OVER_50, ADDITIONAL_DISTANCE_PER_8KM - 1);
        }
        return ageDiscountPolicy.discount(fare);
    }

    private int addExtraFare(final int distance, final int distanceUnit, final int limit) {
        return (int) Math.ceil((double) (distance - limit) / distanceUnit) * ADDITIONAL_AMOUNT;
    }
}
