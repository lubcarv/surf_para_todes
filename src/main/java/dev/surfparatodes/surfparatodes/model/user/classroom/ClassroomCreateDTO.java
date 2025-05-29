package dev.surfparatodes.surfparatodes.model.user.classroom;

import java.util.Set;

public class ClassroomCreateDTO {

    private Set<Integer> teacherIds;

    public Set<Integer> getTeacherIds() {
        return teacherIds;
    }

    public void setTeacherIds(Set<Integer> teacherIds) {
        this.teacherIds = teacherIds;
    }

}
