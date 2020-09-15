package deliberative.bustillos_Quelali;

import logist.plan.Action.*;
import logist.simulation.Vehicle;
import logist.task.Task;
import logist.topology.Topology.City;

import java.util.*;

public class AuxiliarOperations {

    public State clone(State state) {
        return new State(state.currentCity, state.packagesToPickup, state.packagesToDelivery, state.actions, state.capacity);
    }

    public List<State> getSuccessors(State state) {
        List<State> successors = new ArrayList<>();
        for (Task taskPickup : state.packagesToPickup) {
            if (isPossiblePickup(state, taskPickup)) {
                State successor = clone(state);
                if (!taskPickup.pickupCity.equals(state.currentCity)) {
                    List<City> path = successor.getCurrentCity().pathTo(taskPickup.pickupCity);
                    processMoves(successor, path);
                    successor.currentCity = taskPickup.pickupCity;
                }
                processPickup(taskPickup, successor);
                successors.add(successor);
            }
        }
        for (Task taskDelivery : state.packagesToDelivery) {
            State successor = clone(state);
            if (!taskDelivery.deliveryCity.equals(state.currentCity)) {
                List<City> path = successor.getCurrentCity().pathTo(taskDelivery.deliveryCity);
                processMoves(successor, path);
                successor.currentCity = taskDelivery.deliveryCity;
            }
            processDelivery(taskDelivery, successor);
            successors.add(successor);
        }
        return successors;
    }

    public Double realCost(Vehicle vehicle, City city1, City city2) {
        return city1.distanceTo(city2) * vehicle.costPerKm();
    }

    /*
     * Heuristic that provides the cost that the agent will spend if it goes to the farthest city
     * */
    public double h1(Vehicle vehicle, State state) {
        double maxDistance = 0.0;
        City farthestCity = null;

        for (Task task : state.getPackagesToPickup()) {
            Double distance = state.getCurrentCity().distanceTo(task.pickupCity);
            if (distance > maxDistance) {
                maxDistance = distance;
                farthestCity = task.pickupCity;
            }
        }
        for (Task task : state.getPackagesToDelivery()) {
            double distance = state.getCurrentCity().distanceTo(task.deliveryCity);
            if (distance > maxDistance) {
                maxDistance = distance;
                farthestCity = task.deliveryCity;
            }
        }

        return realCost(vehicle, state.getCurrentCity(), farthestCity);
    }

    /*
     * Heuristic that calculates the sumatory of the cost that the agent has to spend
     *  by moving between cities for pickup or deliver a package.
     *
     * When the agent deliver a package, it obtains a reward for it.
     * */
    public double h2(Vehicle vehicle, State state) {
        double cost = 0.0;
        for (Task task : state.getPackagesToPickup()) {
            //cost = cost + realCost(vehicle, state.getCurrentCity(), task.pickupCity);
            cost = cost + realCost(vehicle, state.getCurrentCity(), task.pickupCity) - task.reward;
        }
       /* for (Task task : state.getPackagesToDelivery()) {
            //double netCost = task.reward - realCost(vehicle, state.getCurrentCity(), task.deliveryCity);
            double netCost = realCost(vehicle, state.getCurrentCity(), task.deliveryCity) - task.reward;
            cost = cost + netCost;
        }*/
        return cost;

    }

    public double h3(State state) {
        if (state.getPackagesToPickup().size() != 0 && state.getPackagesToDelivery().size() != 0) {
            return state.getPackagesToDelivery().size() / state.getPackagesToPickup().size();
        }
        return 0;
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

    /*
     * Getting the available capacity of the vehicle
     * */
    private double availableCapacity(State state) {
        double currentCapacity = state.packagesToPickup.weightSum();
        return state.getCapacity() - currentCapacity;
    }

    /*
     * The agent can pickup a package if it has available capacity in the vehicle
     * */
    private boolean isPossiblePickup(State state, Task taskPickup) {
        return (availableCapacity(state) > 0 && taskPickup.weight <= availableCapacity(state));
    }

    /*
     * A terminal state is the state that doesn't have any package to pickup or deliver.
     * */
    public boolean isGoalState(State state) {
        return state.packagesToDelivery.isEmpty() && state.packagesToPickup.isEmpty();
    }
}
