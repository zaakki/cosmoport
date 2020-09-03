package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.*;

@RestController
public class ShipController {
    private final ShipService shipService;


    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }


    @GetMapping(value = "/rest/ships")
    @ResponseBody
    public ResponseEntity<List<Ship>> read(@RequestParam(required = false, name = "name") String name,
                                           @RequestParam(required = false, name = "planet") String planet,
                                           @RequestParam(required = false, name = "shipType") ShipType shipType,
                                           @RequestParam(required = false, name = "after") Long after,
                                           @RequestParam(required = false, name = "before") Long before,
                                           @RequestParam(required = false, name = "isUsed") Boolean isUsed,
                                           @RequestParam(required = false, name = "minSpeed") Double minSpeed,
                                           @RequestParam(required = false, name = "maxSpeed") Double maxSpeed,
                                           @RequestParam(required = false, name = "minCrewSize") Integer minCrewSize,
                                           @RequestParam(required = false, name = "maxCrewSize") Integer maxCrewSize,
                                           @RequestParam(required = false, name = "minRating") Double minRating,
                                           @RequestParam(required = false, name = "maxRating") Double maxRating,
                                           @RequestParam(required = false, name = "order", defaultValue = "") ShipOrder order,
                                           @RequestParam(required = false, name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                           @RequestParam(required = false, name = "pageSize", defaultValue = "3") Integer pageSize) {


        final List<Ship> ships = shipService.readAll();
        List<Ship> shipList = new ArrayList<>();
        if (!ships.isEmpty()) {
            if (name != null) {
                shipList.addAll(Objects.requireNonNull(getShipsWithNameFilter(name, order, ships, pageSize, pageNumber)));
            } else if (planet != null) {
                shipList.addAll(Objects.requireNonNull(getShipsWithPlanetFilter(planet, ships, pageSize, pageNumber)));
            } else if (shipType != null) {
                if (before != null && after != null) {
                    shipList.addAll(Objects.requireNonNull(getShipsWithTypeAndAfterBeforeFilter(shipType, after, before, ships, pageSize, pageNumber)));
                } else if (minSpeed != null && maxSpeed != null) {
                    shipList.addAll(Objects.requireNonNull(getShipsWithTypeAndMinMaxSpeedFilter(shipType, minSpeed, maxSpeed, ships, pageSize, pageNumber)));
                } else if (minCrewSize != null && maxCrewSize != null) {
                    shipList.addAll(Objects.requireNonNull(getShipsWithTypeAndMinMaxCrewSize(shipType, minCrewSize, maxCrewSize, ships, pageSize, pageNumber)));
                }
            } else if (isUsed != null) {
                if (minRating != null && maxRating != null) {
                    shipList.addAll(Objects.requireNonNull(getShipsWithUsedAndMinMaxRating(isUsed, minRating, maxRating, ships, pageSize, pageNumber)));
                }
                if (maxSpeed != null && maxRating != null) {
                    shipList.addAll(Objects.requireNonNull(getShipsWithUsedAndMaxSpeedAndMaxRatingFilter(isUsed, maxSpeed, maxRating, ships, pageSize, pageNumber)));
                }
            } else if (before != null && after != null && minCrewSize != null && maxCrewSize != null) {
                shipList.addAll(Objects.requireNonNull(getShipsWithAfterBeforeAndMinMaxCrewSizeFilter(before, after, minCrewSize, maxCrewSize, ships, pageSize, pageNumber)));
            } else {
                shipList.addAll(Objects.requireNonNull(getShipsWithoutFilter(pageSize, pageNumber, ships)));
            }
        }


        return !ships.isEmpty()
                ? new ResponseEntity<>(shipList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping(value = "/rest/ships/count")
    public ResponseEntity<Integer> count(@RequestParam(required = false, name = "name") String name,
                                         @RequestParam(required = false, name = "planet") String planet,
                                         @RequestParam(required = false, name = "shipType") ShipType shipType,
                                         @RequestParam(required = false, name = "after") Long after,
                                         @RequestParam(required = false, name = "before") Long before,
                                         @RequestParam(required = false, name = "isUsed") Boolean isUsed,
                                         @RequestParam(required = false, name = "minSpeed") Double minSpeed,
                                         @RequestParam(required = false, name = "maxSpeed") Double maxSpeed,
                                         @RequestParam(required = false, name = "minCrewSize") Integer minCrewSize,
                                         @RequestParam(required = false, name = "maxCrewSize") Integer maxCrewSize,
                                         @RequestParam(required = false, name = "minRating") Double minRating,
                                         @RequestParam(required = false, name = "maxRating") Double maxRating) {
        final List<Ship> ships = shipService.readAll();
        List<Ship> shipsList = new ArrayList<>();
        if (!ships.isEmpty()) {
            if (minRating != null && minCrewSize != null && minSpeed != null) {
                shipsList.addAll(Objects.requireNonNull(getShipsWithMinRatingAndCrewSizeAndMinSpeedFilter(minRating, minCrewSize, minSpeed, ships)));
            } else if (name != null && after != null && maxRating != null) {
                shipsList.addAll(Objects.requireNonNull(getShipsWithNameAndAfterAndMaxRatingFilter(name, after, maxRating, ships)));
            } else if (shipType != null) {
                if (isUsed != null) {
                    shipsList.addAll(Objects.requireNonNull(getShipsWithShipTypeAndIsUsedFilter(shipType, isUsed, ships)));
                } else if (maxCrewSize != null) {
                    shipsList.addAll(Objects.requireNonNull(getShipsWithShipTypeAndMaxCrewSizeFilter(shipType, maxCrewSize, ships)));
                } else if (before != null && maxSpeed != null) {
                    shipsList.addAll(Objects.requireNonNull(getShipsWithShipTypeAndBeforeAndMaxSpeedFilter(shipType, before, maxSpeed, ships)));
                }
            } else if (isUsed != null && minSpeed != null && maxSpeed != null) {
                shipsList.addAll(Objects.requireNonNull(getShipsWithIsUsedAndMinSpeedAndMaxSpeedFilter(isUsed, minSpeed, maxSpeed, ships)));
            } else if (planet != null) {
                shipsList.addAll(Objects.requireNonNull(getShipsWithPlanetFilter(planet, ships)));
            } else {
                shipsList.addAll(ships);
            }
        }

        return new ResponseEntity<>(shipsList.size(), HttpStatus.OK);

    }

    @GetMapping(value = "/rest/ships/{id}")
    public ResponseEntity<Ship> read(@PathVariable(name = "id") Long id) {
        final Ship ship = shipService.read(id);

        if (id < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        return ship != null
                ? new ResponseEntity<>(ship, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping(value = "/rest/ships")
    public ResponseEntity<?> create(@RequestBody Ship ship) {
        final boolean created = shipService.create(ship);
        return created
                ? new ResponseEntity<>(ship, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/rest/ships/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id, @RequestBody Ship ship) {
        ResponseEntity<Ship> BAD_REQUEST = getShipResponseEntity(id, ship);
        if (BAD_REQUEST != null) return BAD_REQUEST;
        final boolean updated = shipService.update(ship, id);
        if (updated) {

            final Ship ship1 = shipService.read(id);
            return new ResponseEntity<>(ship1, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/rest/ships/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        if (id == 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        final boolean deleted = shipService.delete(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Ship> getShipResponseEntity(Long id, Ship ship) {
        if (id == 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (ship.getName() != null && (ship.getName().length() > 50 || ship.getName().length() == 0))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (ship.getProdDate() != null && (ship.getProdDate().getYear() < new Date(26213566372158L).getYear() || ship.getProdDate().getYear() > new Date(33124443172158L).getYear()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (ship.getCrewSize() != null && (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return null;
    }

    private List<Ship> getShipsWithAfterBeforeAndMinMaxCrewSizeFilter(Long before, Long after, Integer minCrewSize, Integer maxCrewSize, List<Ship> ships, Integer pageSize, Integer pageNumber) {
        List<Ship> shipsList = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getProdDate().before(new Date(before))
                    && ship.getProdDate().after(new Date(after))
                    && minCrewSize <= ship.getCrewSize()
                    && ship.getCrewSize() <= maxCrewSize) {
                shipsList.add(ship);
            }
        }
        return getShipsWithoutFilter(pageSize, pageNumber, shipsList);
    }

    private List<Ship> getShipsWithUsedAndMaxSpeedAndMaxRatingFilter(Boolean isUsed, Double maxSpeed, Double maxRating, List<Ship> ships, Integer pageSize, Integer pageNumber) {
        List<Ship> shipsList = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getUsed().equals(isUsed)
                    && ship.getSpeed() <= maxSpeed
                    && ship.getRating() <= maxRating) {
                shipsList.add(ship);
            }
        }
        return getShipsWithoutFilter(pageSize, pageNumber, shipsList);
    }

    private List<Ship> getShipsWithUsedAndMinMaxRating(Boolean isUsed, Double minRating, Double maxRating, List<Ship> ships, Integer pageSize, Integer pageNumber) {
        List<Ship> shipsList = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getUsed().equals(isUsed)
                    && minRating <= ship.getRating()
                    && ship.getRating() <= maxRating) {
                shipsList.add(ship);
            }
        }
        return getShipsWithoutFilter(pageSize, pageNumber, shipsList);
    }

    private List<Ship> getShipsWithTypeAndMinMaxCrewSize(ShipType shipType, Integer minCrewSize, Integer maxCrewSize, List<Ship> ships, Integer pageSize, Integer pageNumber) {
        List<Ship> shipsList = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getShipType().equals(shipType)
                    && minCrewSize <= ship.getCrewSize()
                    && ship.getCrewSize() <= maxCrewSize) {
                shipsList.add(ship);
            }
        }
        return getShipsWithoutFilter(pageSize, pageNumber, shipsList);
    }

    private List<Ship> getShipsWithTypeAndMinMaxSpeedFilter(ShipType shipType, Double minSpeed, Double maxSpeed, List<Ship> ships, Integer pageSize, Integer pageNumber) {
        List<Ship> shipsList = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getShipType().equals(shipType)
                    && minSpeed <= ship.getSpeed()
                    && ship.getSpeed() <= maxSpeed) {
                shipsList.add(ship);
            }
        }
        return getShipsWithoutFilter(pageSize, pageNumber, shipsList);
    }

    private List<Ship> getShipsWithTypeAndAfterBeforeFilter(ShipType shipType, Long after, Long before, List<Ship> ships, Integer pageSize, Integer pageNumber) {
        List<Ship> shipsList = new ArrayList<>();

        for (Ship ship : ships) {
            if (ship.getShipType().equals(shipType)
                    && ship.getProdDate().before(new Date(before))
                    && ship.getProdDate().after(new Date(after))) {
                shipsList.add(ship);
            }
        }
        return getShipsWithoutFilter(pageSize, pageNumber, shipsList);
    }

    private List<Ship> getShipsWithNameFilter(String name, ShipOrder order, List<Ship> ships, Integer pageSize, Integer pageNumber) {
        List<Ship> shipsList = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getName().contains(name)) {
                shipsList.add(ship);
            }
        }
        if (order != null) {
            sortByOrder(order, shipsList);
        }
        return getShipsWithoutFilter(pageSize, pageNumber, shipsList);
    }

    private List<Ship> getShipsWithPlanetFilter(String planet, List<Ship> ships, Integer pageSize, Integer pageNumber) {
        List<Ship> shipsList = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getPlanet().contains(planet)) {
                shipsList.add(ship);
            }
        }
        return getShipsWithoutFilter(pageSize, pageNumber, shipsList);
    }

    private List<Ship> getShipsWithoutFilter(Integer pageSize, Integer pageNumber, List<Ship> ships) {
        int lastShipIndex = Math.min(pageNumber * pageSize + pageSize, ships.size());
        return ships.subList(pageNumber * pageSize, lastShipIndex);
    }

    private List<Ship> getShipsWithPlanetFilter(String planet, List<Ship> ships) {
        List<Ship> shipsList = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getPlanet().contains(planet)) {
                shipsList.add(ship);
            }
        }
        return shipsList;
    }

    private List<Ship> getShipsWithIsUsedAndMinSpeedAndMaxSpeedFilter(Boolean isUsed, Double minSpeed, Double maxSpeed, List<Ship> ships) {
        List<Ship> shipsList = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getUsed() == isUsed
                    && minSpeed <= ship.getSpeed()
                    && ship.getSpeed() <= maxSpeed) {
                shipsList.add(ship);
            }
        }
        return shipsList;
    }

    private List<Ship> getShipsWithShipTypeAndBeforeAndMaxSpeedFilter(ShipType shipType, Long before, Double maxSpeed, List<Ship> ships) {
        List<Ship> shipsList = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getShipType().equals(shipType)
                    && ship.getProdDate().before(new Date(before))
                    && ship.getSpeed() <= maxSpeed) {
                shipsList.add(ship);
            }
        }
        return shipsList;
    }

    private List<Ship> getShipsWithShipTypeAndMaxCrewSizeFilter(ShipType shipType, Integer maxCrewSize, List<Ship> ships) {
        List<Ship> shipsList = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getShipType().equals(shipType) && ship.getCrewSize() <= maxCrewSize) {
                shipsList.add(ship);
            }
        }
        return shipsList;
    }

    private List<Ship> getShipsWithShipTypeAndIsUsedFilter(ShipType shipType, Boolean isUsed, List<Ship> ships) {
        List<Ship> shipsList = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getShipType().equals(shipType)
                    && ship.getUsed() == isUsed) {
                shipsList.add(ship);
            }
        }
        return shipsList;
    }

    private List<Ship> getShipsWithNameAndAfterAndMaxRatingFilter(String name, Long after, Double maxRating, List<Ship> ships) {
        List<Ship> shipsList = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getName().contains(name)
                    && ship.getProdDate().after(new Date(after))
                    && ship.getRating() <= maxRating) {
                shipsList.add(ship);
            }
        }
        return shipsList;
    }

    private List<Ship> getShipsWithMinRatingAndCrewSizeAndMinSpeedFilter(Double minRating, Integer minCrewSize, Double minSpeed, List<Ship> ships) {
        List<Ship> shipsList = new ArrayList<>();
        for (Ship ship : ships) {
            if (minRating <= ship.getRating()
                    && minCrewSize <= ship.getCrewSize()
                    && minSpeed <= ship.getSpeed()) {
                shipsList.add(ship);
            }
        }
        return shipsList;
    }

    private void sortByOrder(ShipOrder order, List<Ship> shipList) {
        shipList.sort((ship1, ship2) -> {
            switch (order.getFieldName()) {
                case "rating":
                    return ship1.getRating().compareTo(ship2.getRating());
                case "speed":
                    return ship1.getSpeed().compareTo(ship2.getSpeed());
                case "prodDate":
                    return ship1.getProdDate().compareTo(ship2.getProdDate());
                default:
                    return ship1.getId().compareTo(ship2.getId());
            }
        });
    }

}
