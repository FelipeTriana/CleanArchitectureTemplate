package cleanarchitecture.domain.todo;

import cleanarchitecture.domain.common.ex.BusinessException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static cleanarchitecture.domain.common.StringUtils.isEmpty;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskToDoFactory {

    public static Mono<TaskToDo> createTask(String id, String name, String desc){
        return isEmpty(id, name, desc)
            ? Mono.error(BusinessException.Type.INVALID_TODO_INITIAL_DATA.build())
            : just(TaskToDo.builder().id(id).name(name).description(desc).reportStatus(TaskToDo.TaskReportStatus.PENDING_ASSIGNMENT).build());
    }
}
