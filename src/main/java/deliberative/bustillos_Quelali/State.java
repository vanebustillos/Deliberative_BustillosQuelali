package deliberative.bustillos_Quelali;

import logist.plan.Action;
import logist.task.TaskSet;
import logist.topology.Topology.City;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class State {
    City currentCity;
    TaskSet packagesToPickup;
    TaskSet packagesToDelivery;
    List<Action> actions;
    int capacity;
    int depth;

    public State(City currentCity, TaskSet packagesToPickup, TaskSet packagesToDelivery, List<Action> actions, int capacity, int depth) {
        this.currentCity = currentCity;
        this.packagesToPickup = packagesToPickup.clone();
        this.packagesToDelivery = packagesToDelivery.clone();
        this.actions = new ArrayList<>(actions);
        this.capacity = capacity;
        this.depth = depth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return capacity == state.capacity &&
                depth == state.depth &&
                currentCity.equals(state.currentCity) &&
                Objects.equals(packagesToPickup, state.packagesToPickup) &&
                Objects.equals(packagesToDelivery, state.packagesToDelivery) &&
                Objects.equals(actions, state.actions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentCity, packagesToPickup, packagesToDelivery, actions, capacity, depth);
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public TaskSet getPackagesToPickup() {
        return packagesToPickup;
    }

    public TaskSet getPackagesToDelivery() {
        return packagesToDelivery;
    }

    public List<Action> getActions() {
        return actions;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getDepth() {
        return depth;
    }

}