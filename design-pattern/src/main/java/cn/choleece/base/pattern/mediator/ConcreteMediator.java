package cn.choleece.base.pattern.mediator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author choleece
 * @Description: 具体的中介
 * @Date 2020-04-25 16:13
 **/
public class ConcreteMediator extends Mediator {

    private Map<String, Colleague> colleagueMap = null;

    public ConcreteMediator() {
        colleagueMap = new HashMap();
    }

    @Override
    public void register(String colleagueName, Colleague colleague) {
        colleagueMap.put(colleagueName, colleague);
    }

    @Override
    public void getMessage(int stateChange, String colleagueName) {
        if (colleagueMap.get(colleagueName) instanceof Alarm) {

            TV tv = (TV) colleagueMap.get("tv");
            if (stateChange == 0) {
                tv.startTV();
            } else if (stateChange == 1) {
                tv.stopTV();
            }
        } else if (colleagueMap.get(colleagueName) instanceof TV) {
            Alarm alarm = (Alarm) colleagueMap.get("alarm");

            if (stateChange == 0) {
                alarm.startAlarm();
            } else if (stateChange == 1) {
                alarm.stopAlarm();
            }
        }
    }
}
