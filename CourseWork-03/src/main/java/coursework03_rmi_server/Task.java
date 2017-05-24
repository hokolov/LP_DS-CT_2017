package coursework03_rmi_server;

import java.io.IOException;

public interface Task<T> {
    T execute() throws IOException;
}
