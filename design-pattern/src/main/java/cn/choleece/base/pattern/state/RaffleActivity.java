package cn.choleece.base.pattern.state;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-06 18:59
 **/
public class RaffleActivity {

    public State currentState = null;

    public int count = 0;

    public State noRaffleState = new NoRaffleState(this);

    public State canRaffleState = new CanRaffleState(this);

    public State dispenseState = new DispenseState(this);

    public State dispenseOutState = new DispenseOutState(this);

    public RaffleActivity(int count) {
        this.currentState = noRaffleState;
        this.count = count;
    }

    public void deduce() {
        this.currentState.deduceMoney();
    }

    public void raffle() {
        if (this.currentState.raffle()) {
            this.currentState.dispensePrize();
        }
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public int getCount() {
        int curCount = this.count;
        count--;
        return curCount;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public State getNoRaffleState() {
        return noRaffleState;
    }

    public void setNoRaffleState(State noRaffleState) {
        this.noRaffleState = noRaffleState;
    }

    public State getCanRaffleState() {
        return canRaffleState;
    }

    public void setCanRaffleState(State canRaffleState) {
        this.canRaffleState = canRaffleState;
    }

    public State getDispenseState() {
        return dispenseState;
    }

    public void setDispenseState(State dispenseState) {
        this.dispenseState = dispenseState;
    }

    public State getDispenseOutState() {
        return dispenseOutState;
    }

    public void setDispenseOutState(State dispenseOutState) {
        this.dispenseOutState = dispenseOutState;
    }
}
