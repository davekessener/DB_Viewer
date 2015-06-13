package viewer.tools;

public class ObservableEvent
{
    private Observable cause_;
    
    public ObservableEvent(Observable cause)
    {
        this.cause_ = cause;
    }
    
    public Observable get()
    {
        return cause_;
    }
}
