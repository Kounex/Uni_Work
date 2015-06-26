package serverClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Kounex on 25.06.15.
 */
public class RMIClient{
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(Constants.RMI_PORT);
        RMIServer server = (RMIServer)registry.lookup(Constants.RMI_ID);
    }
}
