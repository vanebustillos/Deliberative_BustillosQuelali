package deliberative.bustillos_Quelali;

import logist.plan.Action;
import logist.plan.Action.*;
import logist.task.Task;
import logist.task.TaskSet;
import logist.topology.Topology.City;

import java.util.*;

public class AuxiliarOperations {

    public State clone(State state) {
        return new State(state.currentCity, state.packagesToPickup, state.packagesToDelivery, state.actions);
    }

    public List<State> getSuccessors(State state, int capacity) {
        List<State> successors = new ArrayList<>();

        for (Task taskPickup: state.packagesToPickup) {
            if(isPossiblePickup(state, taskPickup, capacity)) {
                State successor = clone(state);
                if (taskPickup.pickupCity.equals(state.currentCity)) {
                    processPickup(taskPickup, successor);
                } else {
                    List<City> path = successor.getCurrentCity().pathTo(taskPickup.pickupCity);
                    processMoves(successor, path);
                    successor.currentCity = taskPickup.pickupCity;
                    processPickup(taskPickup, successor);
                }
                successors.add(successor);
            }
        }
        for (Task taskDelivery: state.packagesToDelivery) {
            State successor = clone(state);
            if (taskDelivery.deliveryCity.equals(state.currentCity)) {
                processDelivery(taskDelivery, successor);
            } else {
                List<City> path = successor.getCurrentCity().pathTo(taskDelivery.deliveryCity);
                processMoves(successor, path);
                successor.currentCity = taskDelivery.deliveryCity;
                processDelivery(taskDelivery, successor);
            }
            successors.add(successor);
        }
        return successors;
    }

    private void processDelivery(Task taskDelivery, State successor) {
        successor.actions.add(new Delivery(taskDelivery));
        successor.packagesToDelivery.remove(taskDelivery);
    }

    private void processPickup(Task taskPickup, State successor) {
        successor.actions.add(new Pickup(taskPickup));
        successor.packagesToDelivery.add(taskPickup);
        successor.packagesToPickup.remove(taskPickup);
    }

    private void processMoves(State successor, List<City> path) {
        for (City city : path) {
            successor.actions.add(new Move(city));
        }
    }
    private double availableCapacity(State state, int capacity){
        double currentCapacity = state.packagesToPickup.weightSum();
        return capacity - currentCapacity;
    }

    private boolean isPossiblePickup(State state, Task taskPickup, int capacity){
        return (availableCapacity(state,capacity) > 0 && taskPickup.weight <= availableCapacity(state,capacity));
    }

    public boolean isGoalState(State state) {
        return state.packagesToDelivery.isEmpty() && state.packagesToPickup.isEmpty();
    }
}
