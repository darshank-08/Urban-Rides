package com.example.journalApp.Service;

import com.example.journalApp.Entity.Car;
import com.example.journalApp.Entity.User;
import com.example.journalApp.Repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    // To add car
    public Car addCar(Car req, User user) {

        // Backend controlled data
        req.setOwnerId(user.getId());
        req.setOwnerName(user.getUserName());
        req.setStatus("PENDING_APPROVAL");
        req.setCreatedAt(LocalDateTime.now());

        return carRepository.save(req);
    }

    // To get Cars list
    public List<Car> myCars(String userId){
        return carRepository.ownerId(userId);
    }

    // To delete car
    public void deleteCar(String carID){
        carRepository.deleteById(carID);
    }

    // To update cars
    public Car updateCar(String carId, Car req, User user) {

        Car existing = carRepository.findById(carId)
                .orElse(null);

        if (!existing.getOwnerId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to update this car");
        }

        // Update only editable fields
        existing.setCompany(req.getCompany());
        existing.setModel(req.getModel());
        existing.setYear(req.getYear());
        existing.setImages(req.getImages());
        existing.setDocument(req.getDocument());
        existing.setPricePerDay(req.getPricePerDay());
        existing.setLocation(req.getLocation());
        existing.setSeats(req.getSeats());
        existing.setApproxMileage(req.getApproxMileage());
        existing.setCondition(req.getCondition());

        // When updated → send pending for review again
        existing.setStatus("PENDING_APPROVAL");

        return carRepository.save(existing);
    }

}

