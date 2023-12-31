package cleanarchitecture.web.task;

import cleanarchitecture.domain.todo.TaskToDo;
import cleanarchitecture.domain.user.User;
import cleanarchitecture.usecase.todo.QueryTasksUseCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/task", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class QueryTaskServices {

    private final QueryTasksUseCase useCase;

    @GetMapping(path = "/{id}")
    public Mono<TaskWithUser> findWithDetails(@PathVariable("id") String id){
        return useCase.findTodoWithDetails(id)
            .map(t -> new TaskWithUser(t.getT1(), t.getT2()));
    }

    @GetMapping
    public Flux<TaskToDo> getAll(){
        return useCase.findAll();
    }

    @Data
    @AllArgsConstructor
    private static class TaskWithUser {
        private TaskToDo task;
        private User user;
    }
}
