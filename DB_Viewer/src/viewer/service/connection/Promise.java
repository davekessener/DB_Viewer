package viewer.service.connection;

class Promise implements Runnable
{
    private Task<?> task_;
    private Object result_;
    private Exception error_;
    
    public Promise(Task<?> task)
    {
        this.task_ = task;
        this.result_ = null;
        this.error_ = null;
    }
    
    public boolean hasRun() { return task_ == null; }
    public boolean failed() { return error_ != null; }
    
    public Object getResult() throws Exception
    {
        if(!hasRun())
        {
            execute();
        }
        
        if(failed()) throw error_;
        
        return result_;
    }
    
    @Override
    public void run()
    {
        execute();
    }
    
    private synchronized void execute()
    {
        if(!hasRun())
        {
            try
            {
                result_ = task_.execute();
            }
            catch(Exception e)
            {
                error_ = e;
            }
            finally
            {
                task_ = null;
            }
        }
    }
}
