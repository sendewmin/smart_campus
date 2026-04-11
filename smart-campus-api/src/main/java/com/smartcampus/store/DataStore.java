package com.smartcampus.store;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * Singleton class that holds in-memory data stores for rooms, sensors, and sensor readings.
 */
public class DataStore {
    
    private static DataStore instance;
    
    private final Map<String, Room> rooms;
    private final Map<String, Sensor> sensors;
    private final Map<String, List<SensorReading>> sensorReadings;

    /**
     * Private constructor to prevent instantiation.
     */
    private DataStore() {
        this.rooms = new HashMap<>();
        this.sensors = new HashMap<>();
        this.sensorReadings = new HashMap<>();
    }

    /**
     * Gets the singleton instance of DataStore.
     * Uses synchronized block for thread-safe lazy initialization.
     *
     * @return the singleton instance
     */
    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    // Room operations

    public void addRoom(Room room) {
        rooms.put(room.getId(), room);
    }

    public Room getRoom(String id) {
        return rooms.get(id);
    }

    public boolean removeRoom(String id) {
        return rooms.remove(id) != null;
    }

    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    public Map<String, Room> getRoomsMap() {
        return new HashMap<>(rooms);
    }

    // Sensor operations

    public void addSensor(Sensor sensor) {
        sensors.put(sensor.getId(), sensor);
    }

    public Sensor getSensor(String id) {
        return sensors.get(id);
    }

    public boolean removeSensor(String id) {
        return sensors.remove(id) != null;
    }

    public Collection<Sensor> getAllSensors() {
        return sensors.values();
    }

    public Map<String, Sensor> getSensorsMap() {
        return new HashMap<>(sensors);
    }

    // Sensor Reading operations

    public void addSensorReading(String sensorId, SensorReading reading) {
        sensorReadings.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);
    }

    public List<SensorReading> getSensorReadings(String sensorId) {
        return sensorReadings.getOrDefault(sensorId, new ArrayList<>());
    }

    public boolean removeSensorReadings(String sensorId) {
        return sensorReadings.remove(sensorId) != null;
    }

    public Collection<List<SensorReading>> getAllSensorReadings() {
        return sensorReadings.values();
    }

    public Map<String, List<SensorReading>> getSensorReadingsMap() {
        return new HashMap<>(sensorReadings);
    }

    /**
     * Clears all data stores.
     */
    public void clear() {
        rooms.clear();
        sensors.clear();
        sensorReadings.clear();
    }
}
