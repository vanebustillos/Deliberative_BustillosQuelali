package deliberative.bustillos_Quelali;

import logist.task.TaskSet;
import logist.topology.Topology.City;

import java.util.Objects;

public class State {
    City currentCity;
    TaskSet packagesToPickup;
    TaskSet packagesToDelivery;

    public State(City currentCity, TaskSet packagesToPickup, TaskSet packagesToDelivery) {
        this.currentCity = currentCity;
        this.packagesToPickup = packagesToPickup;
        this.packagesToDelivery = packagesToDelivery;
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(City currentCity) {
        this.currentCity = currentCity;
    }

    public TaskSet getPackagesToPickup() {
        return packagesToPickup;
    }

    public void setPackagesToPickup(TaskSet packagesToPickup) {
        this.packagesToPickup = packagesToPickup;
    }

    public TaskSet getPackagesToDelivery() {
        return packagesToDelivery;
    }

    public void setPackagesToDelivery(TaskSet packagesToDelivery) {
        this.packagesToDelivery = packagesToDelivery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return currentCity.equals(state.currentCity) &&
                Objects.equals(packagesToPickup, state.packagesToPickup) &&
                Objects.equals(packagesToDelivery, state.packagesToDelivery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentCity, packagesToPickup, packagesToDelivery);
    }
}
