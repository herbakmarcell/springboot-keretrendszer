package com.herbak.marcell.beadando.frontend;

import com.herbak.marcell.beadando.entity.User;
import com.herbak.marcell.beadando.service.UserService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.grid.Grid;
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

@Route("/student-list")
@PageTitle("Diákok")
@PermitAll
public class StudentListPage extends AppLayout implements BeforeEnterObserver {
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

    @Autowired
    public StudentListPage(UserService userService) {
        this.userService = userService;

        NavigationBar navbar = new NavigationBar();
        addToNavbar(navbar);

        VerticalLayout content = new VerticalLayout();
        content.setAlignItems(FlexComponent.Alignment.CENTER);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        List<User> students = userService.getAllStudentsByTeacher(username);

        class ImprovisedStudent{
            private String fullName;
            private String email;
            private int hoursDriven;
            private int distanceDriven;

            public ImprovisedStudent(String fullName, String email, int hoursDriven, int distanceDriven) {
                this.fullName = fullName;
                this.email = email;
                this.hoursDriven = hoursDriven;
                this.distanceDriven = distanceDriven;
            }

            public String getFullName() {
                return fullName;
            }

            public String getEmail() {
                return email;
            }

            public int getHoursDriven() {
                return hoursDriven;
            }

            public int getDistanceDriven() {
                return distanceDriven;
            }
        }

        List<ImprovisedStudent> newStudents = students.stream().map(student -> new ImprovisedStudent(student.getFullName(),
                student.getEmail(),
                userService.getHoursDriven(student.getUsername()),
                userService.getDistanceDriven(student.getUsername())
        )).toList();
        Grid<ImprovisedStudent> grid = new Grid<>(ImprovisedStudent.class, false);
        grid.addColumn(ImprovisedStudent::getFullName)
                .setHeader("Név");
        grid.addColumn(ImprovisedStudent::getEmail)
                .setHeader("E-mail");
        grid.addColumn(ImprovisedStudent::getHoursDriven)
                .setHeader("Vezetett órák száma");
        grid.addColumn(ImprovisedStudent::getDistanceDriven)
                .setHeader("Vezetett km száma");
        grid.setItems(newStudents);
        content.add(grid);

        setContent(content);
    }


}
