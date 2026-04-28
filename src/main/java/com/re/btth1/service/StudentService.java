package com.re.btth1.service;

import com.re.btth1.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    private List<Student> students = new ArrayList<>();
    private Long currentId = 1L;

    public List<Student> getAll(){
        return students;
    }

    public void save(Student s){
        s.setId(currentId++);
        students.add(s);
    }

    public Student findById(Long id){
        return students.stream()
                .filter(s->s.getId()==id)
                .findFirst()
                .orElse(null);
    }

    public  void update(Student s){
        for (int i = 0 ; i < students.size(); i++){
            if(students.get(i).getId().equals(s.getId())){
                students.set(i,s);
            }
        }
    }
    public void delete(Long id){
        students.removeIf(s->s.getId().equals(id));
    }
}
