package org.byteclues.lib.model;

import java.util.Observable;

/**
 * Created by admin on 19-07-2015..
 */
public class BasicModel extends Observable {
    public BasicModel(){
    }

    public void notifyObservers(Object data){
        setChanged();
        super.notifyObservers(data);
    }
}
