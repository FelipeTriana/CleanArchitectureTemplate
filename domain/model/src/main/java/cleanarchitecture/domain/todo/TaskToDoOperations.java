package cleanarchitecture.domain.todo;

import cleanarchitecture.domain.common.ex.BusinessException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;
import cleanarchitecture.domain.user.User;

import java.util.Date;
import java.util.function.Function;

import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static cleanarchitecture.domain.common.StringUtils.isEmpty;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskToDoOperations {

    public static Function<TaskToDo, Mono<TaskToDo>> markAsDone(Date doneDate){
        return task ->
            task.getReportStatus() != TaskToDo.TaskReportStatus.ASSIGNED ?
                Mono.error(BusinessException.Type.TASK_NOT_ASSIGNED.build()) :
                just(task.toBuilder().doneDate(doneDate).done(true).build());
    }

    public static Function<TaskToDo, TaskToDo> setPendingToReAssign(){
        return task -> task.toBuilder().assignedUserId(null).reportStatus(TaskToDo.TaskReportStatus.PENDING_REASSIGNMENT).build();
    }

    public static Mono<TaskToDo> assignToUser(TaskToDo task, User user){
        return !isEmpty(task.getAssignedUserId()) ? Mono.error(BusinessException.Type.TASK_ALREADY_ASSIGNED.build()) :
            just(task.toBuilder().assignedUserId(user.getId()).reportStatus(TaskToDo.TaskReportStatus.ASSIGNED).build());
    }

}
