package com.herbak.marcell.beadando.frontend;

import com.herbak.marcell.beadando.entity.DrivingClass;
import com.herbak.marcell.beadando.service.LessonService;
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

import java.util.List;

@Route("/delete-lesson")
@PageTitle("Óra törlése")
@PermitAll
public class DeleteLessonPage extends AppLayout implements BeforeEnterObserver {
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

    private LessonService lessonService;

    ComboBox<String> lessonComboBox;
    Span studentSpan;
    Span hoursSpan;
    Span distanceSpan;
    Span descSpan;

    Button submitButton;

    @Autowired
    public DeleteLessonPage(LessonService lessonService) {
        this.lessonService = lessonService;

        NavigationBar navbar = new NavigationBar();
        addToNavbar(navbar);
        VerticalLayout content = new VerticalLayout();
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        String username = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        List<DrivingClass> classes = lessonService.getAllByTeacher(username);
        studentSpan = new Span();
        hoursSpan = new Span();
        distanceSpan = new Span();
        descSpan = new Span();

        lessonComboBox = new ComboBox<>("Óra kiválasztása");
        lessonComboBox.setItems(classes.stream().map(drivingClass -> String.valueOf(drivingClass.getId())).toList());
        lessonComboBox.setAllowCustomValue(false);
        lessonComboBox.setRequired(true);

        lessonComboBox.addValueChangeListener(event -> {
           String selected = event.getValue();
              if (selected != null){
                DrivingClass drivingClass = lessonService.getLessonById(selected);

                studentSpan.setText("Tanuló: " + drivingClass.getStudent().getFullName());

                hoursSpan.setText("Óra hossza: " + drivingClass.getHoursDriven());

                distanceSpan.setText("Megtett távolság: " + drivingClass.getDistanceDriven());

                descSpan.setText("Leírás: " + drivingClass.getDriveDescription());
              }
        });

        submitButton = new Button("Törlés", event -> {
            lessonService.deleteLesson(lessonComboBox.getValue());
            lessonComboBox.setItems(lessonService.getAllByTeacher(username).stream().map(drivingClass -> String.valueOf(drivingClass.getId())).toList());
            Notification.show("Óra sikeresen törölve!", 3000, Notification.Position.TOP_CENTER);
        });

        content.add(lessonComboBox, studentSpan, hoursSpan, distanceSpan, descSpan, submitButton);
        setContent(content);
    }


}
