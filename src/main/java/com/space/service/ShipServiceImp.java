package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

@Service
public class ShipServiceImp implements ShipService {

    private final ShipRepository shipRepository;

    public ShipServiceImp(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public boolean create(Ship ship) {
        if(isNewShip(ship)) {
            shipRepository.save(ship);
            return true;
        }
        return false;
    }

    @Override
    public List<Ship> readAll() {
        return shipRepository.findAll();
    }

    @Override
    public Ship read(Long id) {
            return shipRepository.findById(id).orElse(null);
    }

    @Override
    public boolean update(Ship ship, long id) {
        if (shipRepository.existsById(id)) {
            Ship ship1 = read(id);
            setUpdatedData(ship,ship1);
            shipRepository.save(ship1);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(long id) {
        if (shipRepository.existsById(id)) {
            shipRepository.deleteById(id);
            return true;
        }
        return false;
    }
    private boolean isNewShip(Ship ship){
        int maxLength = 50;
        double minSpeed = 0.01;
        double maxSpeed = 0.99;
        int maxCrewSize = 9999;
        int minCrewSize = 1;
        Date maxDate = new Date();
        maxDate.setYear(1119);
        Date minDate = new Date();
        minDate.setYear(900);
        double k;
        if (ship.getName() == null
                || ship.getPlanet() == null
                || ship.getShipType() == null
                || ship.getSpeed() == null
                || ship.getCrewSize() == null
                || ship.getProdDate() == null)
            return false;
        else if (ship.getName().length() > maxLength || ship.getPlanet().length() > maxLength
                || ship.getName().length() == 0 || ship.getPlanet().length() == 0)
            return false;
        else if (ship.getSpeed() < minSpeed || ship.getSpeed() > maxSpeed)
            return false;
        else if (ship.getCrewSize() < minCrewSize || ship.getCrewSize() > maxCrewSize)
            return false;
        else if (ship.getProdDate().getTime() < 0
                || ship.getProdDate().getYear() > maxDate.getYear()
                || ship.getProdDate().getYear() < minDate.getYear())
            return false;
        else {
            if (ship.getUsed() == null) {
                ship.setUsed(false);
            }
            if (!ship.getUsed())
                k = 1;
            else
                k = 0.5;
            ship.setRating((80 * ship.getSpeed() * k) / (maxDate.getYear() - ship.getProdDate().getYear() + 1));
            return true;
        }
    }
    private void setUpdatedData(Ship ship, Ship ship1) {
        if (ship.getProdDate() != null
                || ship.getSpeed() != null
                || ship.getUsed() != null
                || ship.getShipType() != null
                || ship.getName() != null
                || ship.getPlanet() != null
                || ship.getCrewSize() != null) {
            Date maxDate = new Date();
            maxDate.setYear(1119);
            double k;
            if (ship.getCrewSize() != null)
                ship1.setCrewSize(ship.getCrewSize());
            if (ship.getProdDate() != null)
                ship1.setProdDate(ship.getProdDate());
            if (ship.getName() != null)
                ship1.setName(ship.getName());
            if (ship.getPlanet() != null)
                ship1.setPlanet(ship.getPlanet());
            if (ship.getUsed() != null)
                ship1.setUsed(ship.getUsed());
            if (ship.getSpeed() != null)
                ship1.setSpeed(ship.getSpeed());
            if (ship.getShipType() != null)
                ship1.setShipType(ship.getShipType());
            if (ship.getRating() != null)
                ship1.setRating(ship.getRating());
            if (!ship1.getUsed()) {
                k = 1;
            } else {
                k = 0.5;
            }
            double r = (80 * ship1.getSpeed() * k) / (maxDate.getYear() - ship1.getProdDate().getYear() + 1);
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String rating = decimalFormat.format(r);
            rating = rating.replace(',', '.');
            ship1.setRating(Double.parseDouble(rating));
        }
    }
}
