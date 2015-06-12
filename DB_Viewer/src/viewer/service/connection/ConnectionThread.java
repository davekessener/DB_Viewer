package viewer.service.connection;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

public class ConnectionThread implements Runnable
{
    private Thread thread_;
    private boolean running_;
    private List<Thread> tasks_;
    private Deque<Promise> queue_;
    
    public ConnectionThread()
    {
        thread_ = null;
        running_ = false;
        tasks_ = new ArrayList<Thread>();
        queue_ = new ConcurrentLinkedDeque<Promise>();
    }
    
    public void start()
    {
        thread_ = new Thread(this);
        running_ = true;
        
        thread_.start();
    }
    
    @Override
    public void run()
    {
        while(running_)
        {
            if(!queue_.isEmpty())
            {
                fulfill(queue_.pop());
            }
            
            Sleep(100);
        }
        
        tasks_.forEach(CheckedConsumer(t -> t.join(100)));
    }
    
    public void stop()
    {
        try
        {
            running_ = false;
            thread_.interrupt();
            thread_.join();
        }
        catch(InterruptedException e) { }
    }
    
    public void register(Promise p)
    {
        queue_.addLast(p);
    }
    
    private void fulfill(Promise p)
    {
        Thread t = new Thread(p);
        
        tasks_.add(t);
        
        t.start();
    }
    
    public boolean isRunning() { return running_; }
    
    public static final void Sleep(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException e) { }
    }
    
    private static interface ConsumeChecked<T> { void accept(T t) throws Exception; }
    private static <T> Consumer<T> CheckedConsumer(ConsumeChecked<T> c)
    {
        return t -> { try { c.accept(t); } catch(Exception e) { } };
    }
}
