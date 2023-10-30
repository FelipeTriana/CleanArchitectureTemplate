package cleanarchitecture.usecase.todo;

import cleanarchitecture.domain.common.EventsGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import cleanarchitecture.domain.todo.TaskToDo;
import cleanarchitecture.domain.todo.TaskToDoFactory;
import cleanarchitecture.domain.todo.events.TaskCreated;
import cleanarchitecture.domain.todo.gateway.TaskToDoRepository;

import static cleanarchitecture.domain.common.UniqueIDGenerator.now;
import static cleanarchitecture.domain.common.UniqueIDGenerator.uuid;

@RequiredArgsConstructor
public class CreateTasksUseCase {

    private final TaskToDoRepository tasks;
    private final EventsGateway eventBus;

    public Mono<TaskToDo> createNew(String name, String description) {
        return uuid()
            .flatMap(id -> TaskToDoFactory.createTask(id, name, description))
            .flatMap(tasks::save)
            .flatMap(task -> emitCreatedEvent(task).thenReturn(task));
    }

    private Mono<Void> emitCreatedEvent(TaskToDo task) {
        return now().flatMap(now -> eventBus.emit(new TaskCreated(task, now)));
    }

}
