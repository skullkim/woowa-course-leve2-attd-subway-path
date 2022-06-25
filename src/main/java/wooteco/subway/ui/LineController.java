package wooteco.subway.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.line.Line;
import wooteco.subway.dto.request.LineRequest;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.service.LineService;

@RestController
@Validated
public class LineController {

    private static final String LINE_ID_MIN_RANGE_ERROR = "라인 아이디는 1 이상이여야 합니다.";

    private LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@Validated @RequestBody final LineRequest lineRequest) {
        final LineResponse response = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }
    @GetMapping(value = "/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        final List<Line> lines = lineService.findAll();
        final List<LineResponse> lineResponses = lines.stream()
                .map(this::getLineResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(lineResponses);
    }

    @GetMapping(value = "/lines/{id}")
    public ResponseEntity<LineResponse> showLine(@Min(value = 1L, message = LINE_ID_MIN_RANGE_ERROR) @PathVariable
                                                     final Long id) {
        final LineResponse line = lineService.findById(id);
        return ResponseEntity.ok().body(line);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> updateLine(@Min(value = 1L, message = LINE_ID_MIN_RANGE_ERROR) @PathVariable
                                               final Long id,
                                           @RequestBody final LineRequest lineRequest) {
        lineService.updateLine(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable @Min(value = 1, message = LINE_ID_MIN_RANGE_ERROR)
                                               final Long id) {
        lineService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private LineResponse getLineResponse(final Line it) {
        return new LineResponse(it.getId(), it.getName(), it.getColor());
    }
}
