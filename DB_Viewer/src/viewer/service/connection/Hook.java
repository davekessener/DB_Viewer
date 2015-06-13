package viewer.service.connection;

public interface Hook<T>
{
    void onDone(Future<T> f);
}
