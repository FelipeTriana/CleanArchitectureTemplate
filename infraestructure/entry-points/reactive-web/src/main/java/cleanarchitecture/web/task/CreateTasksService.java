package cleanarchitecture.web.task;

import cleanarchitecture.domain.todo.TaskToDo;
import cleanarchitecture.usecase.todo.CreateTasksUseCase;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CreateTasksService {

    private final CreateTasksUseCase useCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/task", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TaskToDo> createNew(@RequestBody NewTaskData data) {
        return useCase.createNew(data.getName(), data.getDescription());
    }

    @Data
    private static class NewTaskData {
        private String name;
        private String description;
    }
}
