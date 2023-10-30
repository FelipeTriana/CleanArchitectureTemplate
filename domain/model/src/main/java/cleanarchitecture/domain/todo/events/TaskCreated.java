package cleanarchitecture.domain.todo.events;

import cleanarchitecture.domain.todo.TaskToDo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import cleanarchitecture.domain.common.Event;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class TaskCreated implements Event {

    public static final String EVENT_NAME = "todoTasks.task.created";
    private final TaskToDo task;
    private final Date date;

    @Override
    public String name() {
        return EVENT_NAME;
    }

}
