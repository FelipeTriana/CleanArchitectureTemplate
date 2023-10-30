package cleanarchitecture.jpa.todo;

import lombok.Data;
import lombok.NoArgsConstructor;
import cleanarchitecture.domain.todo.TaskToDo.TaskReportStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class TaskToDoData {
    @Id
    private String id;
    private String name;
    private String description;
    private Date doneDate;
    private String assignedUserId;
    private TaskReportStatus reportStatus;
    private boolean done;
}
