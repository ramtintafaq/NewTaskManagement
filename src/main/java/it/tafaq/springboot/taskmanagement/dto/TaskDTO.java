package it.tafaq.springboot.taskmanagement.dto;

import it.tafaq.springboot.taskmanagement.entity.TaskStatusEnum;

public class TaskDTO {
    private String title;
    private String description;
    private TaskStatusEnum status;

    public TaskDTO() {
    }

    public TaskDTO(String title, String description, TaskStatusEnum status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TaskStatusEnum status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
