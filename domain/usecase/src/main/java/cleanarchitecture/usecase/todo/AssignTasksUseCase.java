package cleanarchitecture.usecase.todo;

import cleanarchitecture.domain.common.EventsGateway;
import cleanarchitecture.domain.common.ex.BusinessException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import cleanarchitecture.domain.todo.TaskToDo;
import cleanarchitecture.domain.todo.TaskToDoOperations;
import cleanarchitecture.domain.todo.events.TaskAssigned;
import cleanarchitecture.domain.todo.gateway.TaskToDoRepository;
import cleanarchitecture.domain.user.User;
import cleanarchitecture.domain.user.gateway.UserGateway;

import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.zip;
import static reactor.function.TupleUtils.function;
import static cleanarchitecture.domain.common.UniqueIDGenerator.now;

@RequiredArgsConstructor
public class AssignTasksUseCase {

    private final TaskToDoRepository tasks;
    private final UserGateway users;
    private final EventsGateway eventBus;

    public Mono<Void> assignTask(String taskId, String userId){
        return zip(findTask(taskId), findUser(userId))
            .flatMap(function(TaskToDoOperations::assignToUser))
            .flatMap(tasks::save)
            .flatMap(this::emitAssignedEvent);
    }

    private Mono<Void> emitAssignedEvent(TaskToDo task) {
        return now().flatMap(now -> eventBus.emit(new TaskAssigned(task, now)));
    }

    private Mono<TaskToDo> findTask(String id){
        return tasks.findById(id).switchIfEmpty(Mono.error(BusinessException.Type.TASK_NOT_FOUND.defer()));
    }

    private Mono<User> findUser(String id){
        return users.findById(id).switchIfEmpty(Mono.error(BusinessException.Type.USER_NOT_EXIST.defer()));
    }
}
