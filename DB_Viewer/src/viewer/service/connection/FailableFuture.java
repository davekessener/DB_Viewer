package viewer.service.connection;

public class FailableFuture
{
    private Future<Boolean> future_;
    
    public FailableFuture(Future<Boolean> f)
    {
        future_ = f;
    }
    
    public boolean isDone() { return future_.isDone(); }
    public boolean get() { try { return future_.get().booleanValue(); } catch(Exception e) { return false; } }
}
