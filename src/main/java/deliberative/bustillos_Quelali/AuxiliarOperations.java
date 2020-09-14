package deliberative.bustillos_Quelali;

import logist.plan.Action.*;
import logist.task.Task;
import logist.task.TaskSet;
import logist.topology.Topology.City;

import java.util.*;

public class AuxiliarOperations {

    public State clone(State state) {
        return new State(state.currentCity, state.packagesToPickup, state.packagesToDelivery, state.actions, state.capacity, state.depth);
    }

    public List<State> getSuccessors(State state) {
        List<State> successors = new ArrayList<>();

        for (Task taskPickup: state.packagesToPickup) {
            if(isPossiblePickup(state, taskPickup)) {
                State successor = clone(state);
                if(!taskPickup.pickupCity.equals(state.currentCity)){
                    List<City> path = successor.getCurrentCity().pathTo(taskPickup.pickupCity);
                    processMoves(successor, path);
                    successor.currentCity = taskPickup.pickupCity;
                }
                processPickup(taskPickup, successor);
                successors.add(successor);
            }
        }
        for (Task taskDelivery: state.packagesToDelivery) {
            State successor = clone(state);
            if(!taskDelivery.deliveryCity.equals(state.currentCity)){
                List<City> path = successor.getCurrentCity().pathTo(taskDelivery.deliveryCity);
                processMoves(successor, path);
                successor.currentCity = taskDelivery.deliveryCity;
            }
            processDelivery(taskDelivery, successor);
            successors.add(successor);
        }
        return successors;
    }

    public Double h1(City currentCity, City destinationCity) {
        return currentCity.distanceTo(destinationCity);
    }

    public Double h2(State state) {
        double minDistance = Double.MAX_VALUE;
        for (Task task: state.getPackagesToPickup()) {
            Double distance = state.getCurrentCity().distanceTo(task.pickupCity);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        return minDistance;
    }
    /*
    * Heuristic that provides the current amount of packages in both package list of the state
    * */
    public Packages h3(State state) {
        return new Packages(state.getPackagesToPickup().size(), state.getPackagesToDelivery().size());
    }
    /*
     *Heuristic that provides the sumatory of the size of pickUp and delivery list of the state
     *  */
    public int h4(State state) {
        return state.getPackagesToPickup().size() + state.getPackagesToDelivery().size();
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
    private double availableCapacity(State state){
        double currentCapacity = state.packagesToPickup.weightSum();
        return state.getCapacity() - currentCapacity;
    }

    private boolean isPossiblePickup(State state, Task taskPickup){
        return (availableCapacity(state) > 0 && taskPickup.weight <= availableCapacity(state));
    }

    public boolean isGoalState(State state) {
        return state.packagesToDelivery.isEmpty() && state.packagesToPickup.isEmpty();
    }

    public class Packages {
        int sizePackagesPickup;
        int  sizePackagesDelivery;

        public Packages(int sizePackagesPickup, int sizePackagesDelivery) {
            this.sizePackagesPickup = sizePackagesPickup;
            this.sizePackagesDelivery = sizePackagesDelivery;
        }
        public int getSizePackagesPickup() {
            return sizePackagesPickup;
        }

        public int getSizePackagesDelivery() {
            return sizePackagesDelivery;
        }
    }
}
