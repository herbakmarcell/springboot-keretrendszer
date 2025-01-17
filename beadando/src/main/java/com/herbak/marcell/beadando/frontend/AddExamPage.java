package com.herbak.marcell.beadando.frontend;

import com.herbak.marcell.beadando.entity.User;
import com.herbak.marcell.beadando.service.ExamService;
import com.herbak.marcell.beadando.service.UserService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
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

@Route("/add-exam")
@PageTitle("Vizsga hozzáadása")
@PermitAll
public class AddExamPage extends AppLayout implements BeforeEnterObserver {
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

    private DatePicker examDatePicker;
    private ComboBox<String> studentComboBox;

    private Button submitButton;

    UserService userService;
    ExamService examService;
    int index = 0;
    @Autowired
    public AddExamPage(UserService userService, ExamService examService) {
        this.userService = userService;
        this.examService = examService;

        NavigationBar navbar = new NavigationBar();
        addToNavbar(navbar);

        VerticalLayout content = new VerticalLayout();
        examDatePicker = new DatePicker("Vizsga dátuma");
        studentComboBox = new ComboBox<>("Vizsgázó");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        List<User> students = userService.getAllStudentsByTeacher(username);

        studentComboBox.setItems(students.stream().map(User::getFullName).toList());
        studentComboBox.addValueChangeListener(event -> {
            String selectedItem = event.getValue();
            index = students.stream().map(User::getFullName).toList().indexOf(selectedItem);
        });
        submitButton = new Button("Vizsga hozzáadása");
        submitButton.addClickListener(event -> {
            if(studentComboBox.getValue() == null){
                Notification.show("Kérem jelöljön ki időpontot!", 3000, Notification.Position.MIDDLE);
                return;
            }
            if(examDatePicker.getValue().isBefore(java.time.LocalDate.now())){
                Notification.show("Korábbi alkalomra nem jelölhet ki vizsgát!", 3000, Notification.Position.MIDDLE);
                return;
            }
            examService.addExam(examDatePicker.getValue(), students.get(index), userService.getUserByUsername(username));
            Notification.show("Vizsga sikeresen létrehozva", 3000, Notification.Position.MIDDLE);
        });
        content.add(examDatePicker, studentComboBox, submitButton);
        setContent(content);
    }


}
