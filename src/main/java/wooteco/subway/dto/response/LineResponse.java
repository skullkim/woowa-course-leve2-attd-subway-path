package wooteco.subway.dto.response;

import java.util.List;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Section;
import wooteco.subway.util.Converter;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private List<StationResponse> stations;

    private LineResponse() {
    }

    public LineResponse(final Long id, final String name, final String color, final int extraFare,
                        final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stations = stations;
    }

    public LineResponse(final Long id, final String name, final String color) {
        this(id, name, color, 0, List.of());
    }

    public static LineResponse from(final Line line) {
        final List<Section> sections = line.getSections();
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getExtraFare(),
                Converter.convertFromSections(sections));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getExtraFare() {
        return extraFare;
    }
}
