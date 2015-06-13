package viewer.service.connection;

public class Future<T>
{
    private Promise promise_;
    
    public Future(Promise promise)
    {
        this.promise_ = promise;
    }
    
    public boolean isDone() { return promise_.hasRun(); }
    
    @SuppressWarnings("unchecked")
    public T get() throws Exception { return (T) promise_.getResult(); }
    
    public void onDone(Hook<T> h)
    {
        promise_.onDone(() -> h.onDone(this));
    }
}
