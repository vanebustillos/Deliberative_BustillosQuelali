package deliberative.bustillos_Quelali;

/* import table */
import logist.agent.Agent;
import logist.behavior.DeliberativeBehavior;
import logist.plan.Plan;
import logist.simulation.Vehicle;
import logist.task.Task;
import logist.task.TaskDistribution;
import logist.task.TaskSet;
import logist.topology.Topology;
import logist.topology.Topology.City;

import java.util.LinkedList;

/**
 * An optimal planner for one vehicle.
 */
@SuppressWarnings("unused")
public class DeliberativeTemplate_BustillosQuelali implements DeliberativeBehavior {

	enum Algorithm { BFS, ASTAR }
	
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
		int capacity = agent.vehicles().get(0).capacity();
		String algorithmName = agent.readProperty("algorithm", String.class, "ASTAR");
		
		// Throws IllegalArgumentException if algorithm is unknown
		algorithm = Algorithm.valueOf(algorithmName.toUpperCase());
		
		// ...
	}
	
	@Override
	public Plan plan(Vehicle vehicle, TaskSet tasks) {
		Plan plan;
		// Compute the plan with the selected algorithm.
		switch (algorithm) {
		case ASTAR:
			// ...
			plan = naivePlan(vehicle, tasks);
			break;
		case BFS:
			// ...
			plan = naivePlan(vehicle, tasks);
			break;
		default:
			throw new AssertionError("Should not happen.");
		}		
		return plan;
	}
	
	private Plan naivePlan(Vehicle vehicle, TaskSet tasks) {
		City current = vehicle.getCurrentCity();
		Plan plan = new Plan(current);

		for (Task task : tasks) {
			// move: current city => pickup location
			for (City city : current.pathTo(task.pickupCity)) {
				plan.appendMove(city);
				//System.out.println("Current: " + current + " PickupCity :" + current.pathTo(task.pickupCity));
			}
			plan.appendPickup(task); //Action
			//System.out.println("Task: " + task); // Task: (Task 1, 3 kg, 40875 CHF, Z³rich -> Sion)

			// move: pickup location => delivery location
			for (City city : task.path()) {
				plan.appendMove(city);
				//System.out.println("City2: " + city);
			}
			plan.appendDelivery(task);
			// set current city
			current = task.deliveryCity;
		}
		return plan;
	}
	public void bfs(Vehicle vehicle, TaskSet tasks) {
		Plan plan = new Plan(vehicle.getCurrentCity());
		State initialState = new State(vehicle.getCurrentCity(), tasks, null);
		LinkedList<State> q = new LinkedList<>();
		q.add(initialState);
		do {
			State state = q.removeFirst();
			if (operations.isGoalState(state)) {
				System.out.println("Finish Tasks!");
				break;
			}
			LinkedList<State> s = new LinkedList<>(operations.getSuccessors(state,capacity));
			q.addAll(0,s);

		} while (!q.isEmpty());
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
