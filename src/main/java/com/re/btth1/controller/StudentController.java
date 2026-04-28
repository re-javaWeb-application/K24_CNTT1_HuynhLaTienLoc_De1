package com.re.btth1.controller;

import com.re.btth1.model.Student;
import com.re.btth1.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public String list(
            @RequestParam(defaultValue = "") String keyWord,
            @RequestParam(defaultValue = "studentCode") String sortBy,
            @RequestParam(defaultValue = "asc") String dir,
            Model model
    ){
        List<Student> list = new ArrayList<>(studentService.getAll());
        if (!keyWord.isEmpty()){
            list = list.stream()
                    .filter(s->s.getFullName().toLowerCase().contains(keyWord.toLowerCase())|| s.getStudentCode().toLowerCase().contains(keyWord.toLowerCase()))
                    .toList();
        }
        Comparator<Student>com;
        switch (sortBy){
            case"fullName":
                com = Comparator.comparing(Student::getFullName);
                break;
            case "gpa":
                com = Comparator.comparing(Student::getGpa);
                break;
            default:
                com = Comparator.comparing(Student::getStudentCode);
        }
        if(dir.equals("desc")) com = com.reversed();
        list.sort(com);
        
        model.addAttribute("list", list);
        model.addAttribute("keyword", keyWord);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("dir", dir);
        return "index";
    }
    
    @GetMapping("/")
    public String home(){
        return "redirect:/students";
    }
    
    @GetMapping("/home")
    public String homePath(){
        return "redirect:/students";
    }
    
    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("student", new Student());
        return "form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id , Model model){
        Student student = studentService.findById(id);
        if (student == null) {
            return "redirect:/students";
        }
        model.addAttribute("student", student);
        return "form";
    }
    
    @PostMapping("/save")
    public String save(@ModelAttribute Student student, Model model){
        // Validation
        if (student.getStudentCode() == null || student.getStudentCode().isEmpty()) {
            model.addAttribute("error", "Mã SV không được để trống!");
            model.addAttribute("student", student);
            return "form";
        }
        if (student.getStudentCode().length() < 5 || student.getStudentCode().length() > 100) {
            model.addAttribute("error", "Mã SV phải từ 5 đến 100 ký tự!");
            model.addAttribute("student", student);
            return "form";
        }
        if (student.getFullName() == null || student.getFullName().isEmpty()) {
            model.addAttribute("error", "Tên sinh viên không được để trống!");
            model.addAttribute("student", student);
            return "form";
        }
        if (student.getFullName().length() < 5 || student.getFullName().length() > 100) {
            model.addAttribute("error", "Tên sinh viên phải từ 5 đến 100 ký tự!");
            model.addAttribute("student", student);
            return "form";
        }
        if (student.getEmail() == null || !student.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            model.addAttribute("error", "Email không hợp lệ!");
            model.addAttribute("student", student);
            return "form";
        }
        if (student.getGpa() == null || student.getGpa() < 0.0 || student.getGpa() > 10.0) {
            model.addAttribute("error", "GPA phải từ 0.0 đến 10.0!");
            model.addAttribute("student", student);
            return "form";
        }
        
        if (student.getId() == null) {
            studentService.save(student);
        } else {
            studentService.update(student);
        }
        return "redirect:/students";
    }
    
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        studentService.delete(id);
        return "redirect:/students";
    }
}
