package com.space.service;

import com.space.model.Ship;

import java.util.List;

public interface ShipService {
    /**
     * Create new ship
     * @param ship - ship for created
     * @return
     */
    boolean create(Ship ship);

    /**
     * Return list with all ships
     * @return ship list
     */
    List<Ship> readAll();

    /**
     * Return ship by ID
     * @param id - Ship ID
     * @return - ship by ID
     */
    Ship read(Long id);

    /**
     * Update ship by ID whit new param
     * @param ship - ship according to which you need to update the data
     * @param id - id of the ship to update
     * @return - true if data was updated, else false
     */
    boolean update(Ship ship, long id);

    /**
     * Deleted ship by ID
     * @param id - ship ID for delete
     * @return - true if ship was delete, else false
     */
    boolean delete(long id);


}
