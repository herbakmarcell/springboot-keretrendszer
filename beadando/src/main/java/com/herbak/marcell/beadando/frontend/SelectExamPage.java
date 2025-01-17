package com.herbak.marcell.beadando.frontend;

import com.herbak.marcell.beadando.entity.DrivingExam;
import com.herbak.marcell.beadando.entity.User;
import com.herbak.marcell.beadando.service.ExamService;
import com.herbak.marcell.beadando.service.UserService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

import java.util.Collection;
import java.util.List;

@Route("/select-exam")
@PageTitle("Vizsga kiválasztása")
@PermitAll
public class SelectExamPage extends AppLayout implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            boolean hasRoleSupervisor = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPERVISOR"));

            if (!hasRoleSupervisor) {
                event.forwardTo("/");
            }
        } else {
            event.forwardTo("/login");
        }
    }

    ExamService examService;
    UserService userService;

    ComboBox<String> examComboBox;
    Span examSpan;

    Button submitButton;

    int index;
    @Autowired
    public SelectExamPage(ExamService examService, UserService userService) {
        this.examService = examService;
        this.userService = userService;

        NavigationBar navbar = new NavigationBar();
        addToNavbar(navbar);

        VerticalLayout content = new VerticalLayout();
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);


        List<DrivingExam> exams = examService.getEmptyExams();

        examSpan = new Span();
        examSpan.setText("Kérem jelöljön ki egy vizsgát!");
        examComboBox = new ComboBox<>("Vizsgakód");
        examComboBox.setItems(exams.stream()
                .map(exam -> String.valueOf(exam.getExamId()))
                .toList()
        );

        examComboBox.addValueChangeListener(event -> {
            String selectedItem = event.getValue();
            index = exams.stream().map(DrivingExam::getExamId).toList().indexOf(Long.parseLong(selectedItem));

            examSpan.setText("Vizsga időpontja: " + exams.get(index).getExamDate());
        });

        submitButton = new Button("Kiválaszt");
        submitButton.addClickListener(e -> {
            if (examComboBox.getValue() == null){
                Notification.show("Kérem válasszon ki egy vizsgát!", 3000, Notification.Position.MIDDLE);
                return;
            }

            examService.addSupervisorToExam(examComboBox.getValue(), userService.getUserByUsername(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
            submitButton.getUI().ifPresent(ui -> ui.navigate("/"));
            Notification.show("Vizsga sikeresen felvéve!", 3000, Notification.Position.MIDDLE);
        });

        content.add(examComboBox, examSpan, submitButton);
        setContent(content);
    }
}
