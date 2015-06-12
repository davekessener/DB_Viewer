package viewer.service.connection;

public interface Task<T>
{
    T execute() throws Exception;
}
