package com.herbak.marcell.beadando.service;

import com.herbak.marcell.beadando.dao.DrivingPathDao;
import com.herbak.marcell.beadando.entity.DrivingPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    DrivingPathDao drivingPathDao;

    @Autowired
    public PathService(DrivingPathDao drivingPathDao) {
        this.drivingPathDao = drivingPathDao;
    }

    public void addPath(String pathName, int pathLength) {
        DrivingPath path = new DrivingPath();
        path.setPathName(pathName);
        path.setPathLength(pathLength);

        drivingPathDao.add(path);

    }
}
