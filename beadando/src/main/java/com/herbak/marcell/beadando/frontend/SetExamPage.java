package com.herbak.marcell.beadando.frontend;

import com.herbak.marcell.beadando.entity.DrivingExam;
import com.herbak.marcell.beadando.entity.DrivingPath;
import com.herbak.marcell.beadando.service.ExamService;
import com.herbak.marcell.beadando.service.UserService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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

@Route("/set-exam")
@PageTitle("Vizsgaeredmény beírása")
@PermitAll
public class SetExamPage extends AppLayout implements BeforeEnterObserver {
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            boolean hasRoleTeacher = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPERVISOR"));

            if (!hasRoleTeacher) {
                event.forwardTo("/");
            }
        } else {
            event.forwardTo("/login");
        }
    }

    ExamService examService;
    UserService userService;

    ComboBox<String> examComboBox;
    Span studentSpan;
    Span dateSpan;

    ComboBox<String> pathComboBox;
    IntegerField errorField;
    ComboBox<String> resultComboBox;



    Button submitButton;

    @Autowired
    public SetExamPage(ExamService examService, UserService userService) {
        this.examService = examService;
        this.userService = userService;

        NavigationBar navbar = new NavigationBar();
        VerticalLayout content = new VerticalLayout();
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        examComboBox = new ComboBox<>("Vizsgák");
        List<DrivingExam> exams = examService.getAllUnratedExamsBySupervisor(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        examComboBox.setItems(exams.stream()
                        .map(exam -> String.valueOf(exam.getExamId()))
                        .toList()
        );
        examComboBox.addValueChangeListener(event -> {
            DrivingExam exam = examService.getExamById(Integer.parseInt(examComboBox.getValue()));
            if (exam != null){
                studentSpan = new Span();
                studentSpan.setText("Vizsgázó: " + exam.getStudent().getFullName());
                dateSpan = new Span();
                dateSpan.setText("Dátum: " + exam.getExamDate());
                content.add(studentSpan, dateSpan);

                pathComboBox = new ComboBox<>("Vizsga útvonala");
                pathComboBox.setItems(examService.getDrivingPaths().stream().map(DrivingPath::getPathName).toList());
                errorField = new IntegerField("Hibapontok száma");
                resultComboBox = new ComboBox<>("Eredmény");
                resultComboBox.setItems("Sikeres", "Sikertelen");

                submitButton = new Button("Eredmény felvétele");
                submitButton.addClickListener(e -> {
                    if (examComboBox.getValue() == null){
                        return;
                    }
                    examService.addResultToExam(examComboBox.getValue(),pathComboBox.getValue(), errorField.getValue(), resultComboBox.getValue().equals("Sikeres"));
                    Notification.show("Vizsga eredmény sikeresen beírva!", 3000, Notification.Position.MIDDLE);
                    submitButton.getUI().ifPresent(ui -> ui.navigate("/"));
                });

                content.add(pathComboBox, errorField, resultComboBox, submitButton);

            }
        });

        content.add(examComboBox);
        addToNavbar(navbar);
        setContent(content);
    }

}
