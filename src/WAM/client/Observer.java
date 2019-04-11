package WAM.client;

import javax.security.auth.Subject;

public interface Observer {
    void update(Subject subject);
}
