package deliberative.bustillos_Quelali;

/* import table */

import logist.agent.Agent;
import logist.behavior.DeliberativeBehavior;
import logist.plan.Action;
import logist.plan.Plan;
import logist.simulation.Vehicle;
import logist.task.TaskDistribution;
import logist.task.TaskSet;
import logist.topology.Topology;

import java.util.*;

/**
 * An optimal planner for one vehicle.
 */
@SuppressWarnings("unused")
public class DeliberativeTemplate_BustillosQuelali implements DeliberativeBehavior {

    enum Algorithm {BFS, ASTAR}

    /* Environment */
    Topology topology;
    TaskDistribution td;

    /* the properties of the agent */
    Agent agent;
    int capacity;

    /* the planning class */
    Algorithm algorithm;

    AuxiliarOperations operations = new AuxiliarOperations();

    @Override
    public void setup(Topology topology, TaskDistribution td, Agent agent) {
        this.topology = topology;
        this.td = td;
        this.agent = agent;
        // initialize the planner
        capacity = agent.vehicles().get(0).capacity();
        String algorithmName = agent.readProperty("algorithm", String.class, "ASTAR");

        // Throws IllegalArgumentException if algorithm is unknown
        algorithm = Algorithm.valueOf(algorithmName.toUpperCase());

    }

    @Override
    public Plan plan(Vehicle vehicle, TaskSet tasks) {
        Plan plan;
        switch (algorithm) {
            case ASTAR:
                plan = aStar(vehicle, tasks);
                break;
            case BFS:
                plan = bfs(vehicle, tasks);
                break;
            default:
                throw new AssertionError("Should not happen.");
        }
        return plan;
    }

    private Plan bfs(Vehicle vehicle, TaskSet tasks) {
        Plan plan = new Plan(vehicle.getCurrentCity());
        State initialState = new State(vehicle.getCurrentCity(), tasks, vehicle.getCurrentTasks(), Collections.emptyList(), capacity);
        LinkedList<State> q = new LinkedList<>();
        q.add(initialState);
        do {
            State state = q.removeFirst();
            if (operations.isGoalState(state)) {
                return appendActions(plan, state);
            }
            q.addAll(operations.getSuccessors(state));
        } while (!q.isEmpty());
        return plan;
    }

    private Plan aStar(Vehicle vehicle, TaskSet tasks) {
        Plan plan = new Plan(vehicle.getCurrentCity());
        State initialState = new State(vehicle.getCurrentCity(), tasks, vehicle.getCurrentTasks(), Collections.emptyList(), capacity);
        List<State> border = new ArrayList<>();
        border.add(initialState);
        while (!border.isEmpty()) {
            State bestState = null;

            //-------------------H3
            /*double minHeuristic = Double.MAX_VALUE;
            for (State potentialNext : border) {
                if (operations.h3(potentialNext) < minHeuristic) {
                    minHeuristic = operations.h3(potentialNext);
                    bestState = potentialNext;
                }
            }*/
            //-------------------H2 max
			double maxHeuristic = - Double.MAX_VALUE;
			for (State potentialNext: border) {
				if (operations.h2(vehicle,potentialNext) > maxHeuristic) {
					maxHeuristic = operations.h2(vehicle, potentialNext);
					bestState = potentialNext;
				}
			}
			//-------------------
            //-------------------H2 min
			/*double minHeuristic = Double.MAX_VALUE;
			for (State potentialNext: border) {
				if (operations.h2(vehicle,potentialNext) < minHeuristic) {
					minHeuristic = operations.h2(vehicle, potentialNext);
					bestState = potentialNext;
				}
			}*/
            //-------------------------
            if (bestState == null) {
                throw new IllegalStateException("Unexpected state left.");
            }
            border.remove(bestState);
            if (operations.isGoalState(bestState)) {
                return appendActions(plan, bestState);
            }
            List<State> successors = operations.getSuccessors(bestState);
            border.addAll(successors);
        }
        return plan;
    }

    private Plan appendActions(Plan plan, State bestState) {
        for (Action action : bestState.getActions()) {
            plan.append(action);
        }
        return plan;
    }

    @Override
    public void planCancelled(TaskSet carriedTasks) {

        if (!carriedTasks.isEmpty()) {
            // This cannot happen for this simple agent, but typically
            // you will need to consider the carriedTasks when the next
            // plan is computed.
        }
    }
}
