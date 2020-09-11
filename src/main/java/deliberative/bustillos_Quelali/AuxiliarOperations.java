package deliberative.bustillos_Quelali;

import logist.task.Task;
import logist.topology.Topology.City;

import java.util.*;

public class AuxiliarOperations {

    public State clone(State state) {
        return new State(state.currentCity, state.packagesToPickup, state.packagesToDelivery);
    }

    public List<State> getSuccessors(State state, int capacity){
        List<State> successors = new ArrayList<>();
        if(isGoalState(state)){
            return Collections.emptyList();
        }
        State child = clone(state);

        for (Task pickupTask : child.packagesToPickup) {
            if(pickupTask.pickupCity.equals(child.currentCity)){
                int currentCapacity = child.packagesToPickup.weightSum();
                int availableCapacity = capacity - currentCapacity;
                if(currentCapacity < capacity && pickupTask.weight <= availableCapacity){
                    State successor = clone(child);
                    successor.packagesToDelivery.add(pickupTask);
                    successor.packagesToPickup.remove(pickupTask);
                    successors.add(successor);
                } else{
                    if(!child.packagesToDelivery.isEmpty()){
                        for (Task moveTask: child.packagesToDelivery) {
                            State successor = clone(child);
                            List<City> path = successor.getCurrentCity().pathTo(moveTask.deliveryCity);
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
                successors.add(successor);
            }
        }
        return successors;
    }
    public boolean isGoalState(State state) {
        return state.packagesToDelivery.isEmpty() && state.packagesToPickup.isEmpty();
    }

}
