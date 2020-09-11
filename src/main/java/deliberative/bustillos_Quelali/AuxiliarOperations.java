package deliberative.bustillos_Quelali;

import logist.plan.Action;
import logist.plan.Action.*;
import logist.task.Task;
import logist.topology.Topology.City;

import java.util.*;

public class AuxiliarOperations {

    public State clone(State state) {
        return new State(state.currentCity, state.packagesToPickup, state.packagesToDelivery, state.actions);
    }
/*
    public List<State> getSuccessors(State state, int capacity){
        List<State> successors = new ArrayList<>();
        Action action;
        if(isGoalState(state)){
            return Collections.emptyList();
        }

        for (Task pickupTask : state.packagesToPickup) {
            if(pickupTask.pickupCity.equals(state.currentCity)){
                int currentCapacity = state.packagesToPickup.weightSum();
                int availableCapacity = capacity - currentCapacity;
                if(currentCapacity < capacity && pickupTask.weight <= availableCapacity){
                    State successor = clone(state);
                    successor.packagesToDelivery.add(pickupTask);
                    successor.packagesToPickup.remove(pickupTask);
                    successor.action = new Pickup(pickupTask);
                    successors.add(successor);
                } else{
                    if(!state.packagesToDelivery.isEmpty()){
                        for (Task moveTask: state.packagesToDelivery) {
                            State successor = clone(state);
                            List<City> path = successor.getCurrentCity().pathTo(moveTask.deliveryCity);
                            successor.action = new Move(path.get(0));
                            successor.currentCity = path.get(0);
                            successors.add(successor);
                        }
                    }
                }
            }
        }
        for (Task deliveryTask: child.packagesToDelivery){
            if(deliveryTask.deliveryCity.equals(child.currentCity)){
                State successor = clone(child);
                successor.packagesToDelivery.remove(deliveryTask);
                successor.action = new Delivery(deliveryTask);
                successors.add(successor);
            }
        }
        return successors;
    }*/

    public boolean isGoalState(State state) {
        return state.packagesToDelivery.isEmpty() && state.packagesToPickup.isEmpty();
    }

}
