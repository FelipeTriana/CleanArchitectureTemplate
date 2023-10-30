package cleanarchitecture.jpa.todo;

import com.reactive.repository.jpa.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import cleanarchitecture.domain.todo.TaskToDo;
import cleanarchitecture.domain.todo.gateway.TaskToDoRepository;

@Repository
public class TaskToDoRepositoryAdapter extends AdapterOperations<TaskToDo, TaskToDoData, String, TaskToDoDataRepository> implements TaskToDoRepository {

    @Autowired
    public TaskToDoRepositoryAdapter(TaskToDoDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, TaskToDo.TaskToDoBuilder.class).build());
    }

    @Override
    public Mono<Void> saveAll(Flux<TaskToDo> tasks) {
        return saveAllEntities(tasks).then();
    }

    @Override
    public Flux<TaskToDo> findAllUserOpenTasks(String userId) {
        return doQueryMany(() -> repository.findAllByAssignedUserIdAndDone(userId, false));
    }

    @Override
    public Flux<TaskToDo> findAll() {
        return doQueryMany(repository::findAll);
    }
}
