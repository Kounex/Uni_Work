package serverClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Kounex on 25.06.15.
 */
public interface RMIServer extends Remote{
    default String answer() throws RemoteException{
        return "Don't wake me up!!";
    }
}
