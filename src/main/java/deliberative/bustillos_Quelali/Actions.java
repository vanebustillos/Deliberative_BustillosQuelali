package deliberative.bustillos_Quelali;

import logist.topology.Topology;

public class Actions {

    private final Topology.City destination;
    private final ActionType actionType;

    public enum ActionType{
        PICKUP,
        MOVE,
        DELIVER
    }

    public Actions(ActionType actionType,Topology.City destination) {
        this.destination = destination;
        this.actionType = actionType;
    }

    public Topology.City getDestination() {
        return destination;
    }

    public ActionType getActionType() {
        return actionType;
    }
}
