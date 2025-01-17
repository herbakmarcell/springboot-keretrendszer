package com.herbak.marcell.beadando.frontend;

import com.herbak.marcell.beadando.entity.DrivingClass;
import com.herbak.marcell.beadando.entity.DrivingExam;
import com.herbak.marcell.beadando.entity.User;
import com.herbak.marcell.beadando.service.LessonService;
import com.herbak.marcell.beadando.service.UserService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Route("add-lesson")
@PageTitle("Óra felvétele")
@PermitAll
public class AddLessonPage extends AppLayout implements BeforeEnterObserver {
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            boolean hasRoleTeacher = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TEACHER"));

            if (!hasRoleTeacher) {
                event.forwardTo("/");
            }
        } else {
            event.forwardTo("/login");
        }
    }

    UserService userService;
    LessonService lessonService;

    ComboBox<String> studentComboBox;
    Span idSpan;
    IntegerField hoursField;
    IntegerField distanceField;
    TextField descField;

    Button submitButton;

    int index;

    @Autowired
    public AddLessonPage(UserService userService, LessonService lessonService) {
        this.userService = userService;
        this.lessonService = lessonService;

        NavigationBar navbar = new NavigationBar();
        addToNavbar(navbar);
        VerticalLayout content = new VerticalLayout();

        String teacherUsername = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        List<User> students = userService.getAllStudentsByTeacher(teacherUsername);
        User teacher = userService.getUserByUsername(teacherUsername);
        idSpan = new Span();
        studentComboBox = new ComboBox<>("Tanuló");
        studentComboBox.setItems(students.stream().map(User::getFullName).toList());
        studentComboBox.addValueChangeListener(event -> {
            String selectedItem = event.getValue();
            index = students.stream().map(User::getFullName).toList().indexOf(selectedItem);


            idSpan.setText("ID: " + students.get(index).getId());
        });

        hoursField = new IntegerField("Vezetett órák száma");
        distanceField = new IntegerField("Vezetett km száma");
        descField = new TextField("Leírás");

        submitButton = new Button("Óra felvétele");
        submitButton.addClickListener(e -> {

            lessonService.addLesson(students.get(index), teacher, hoursField.getValue(), distanceField.getValue(), descField.getValue());
            submitButton.getUI().ifPresent(ui -> ui.navigate("/"));
        });


        content.add(studentComboBox, idSpan, hoursField, distanceField, descField, submitButton);
        setContent(content);

    }
}
