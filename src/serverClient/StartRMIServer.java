package serverClient;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Kounex on 25.06.15.
 */
public class StartRMIServer {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        RMIServerImpl server = new RMIServerImpl();
        Registry registry = LocateRegistry.createRegistry(Constants.RMI_PORT);
        registry.bind(Constants.RMI_ID, server);
        System.out.print(RMIServer.class.getName() + " is started");
    }
}
