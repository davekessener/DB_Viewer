package viewer.tools;

import java.util.ArrayList;
import java.util.List;

public class AbstractObservable implements Observable
{
    private List<Observer> observers_ = new ArrayList<>();

    @Override
    public void register(Observer o)
    {
        observers_.add(o);
    }
    
    public void change() { change(new ObservableEvent(this)); }
    public void change(ObservableEvent e)
    {
        for(Observer o : observers_)
        {
            o.onChange(e);
        }
    }
}
