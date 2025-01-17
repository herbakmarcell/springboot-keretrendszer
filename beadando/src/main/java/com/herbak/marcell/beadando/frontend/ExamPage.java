package com.herbak.marcell.beadando.frontend;

import com.herbak.marcell.beadando.entity.DrivingExam;
import com.herbak.marcell.beadando.entity.User;
import com.herbak.marcell.beadando.service.ExamService;
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

@Route("/exams")
@PageTitle("Vizsgák")
@PermitAll
public class ExamPage extends AppLayout implements BeforeEnterObserver {
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

    private ExamService examService;

    @Autowired
    public ExamPage(ExamService examService) {
        this.examService = examService;

        NavigationBar navbar = new NavigationBar();
        addToNavbar(navbar);

        VerticalLayout content = new VerticalLayout();
        content.setAlignItems(FlexComponent.Alignment.CENTER);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        List<DrivingExam> unratedExams = examService.getAllUnratedExamsBySupervisor(username);

        Grid<DrivingExam> grid = new Grid<>(DrivingExam.class, false);
        grid.addColumn(DrivingExam::getExamId).setHeader("ID");
        grid.addColumn(DrivingExam::getExamDate).setHeader("Dátum");
        grid.addColumn(drivingExam -> drivingExam.getStudent().getFullName()).setHeader("Diák");
        grid.setItems(unratedExams);

        content.add(grid);
        setContent(content);
    }
}
