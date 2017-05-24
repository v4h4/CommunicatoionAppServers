package RMI;

import java.rmi.Remote;  
import java.rmi.RemoteException;  
  
public interface RmiInterface extends Remote {  
	
	public int getSumOfList(int[] intList)throws RemoteException;
    
	public int getMaxOfList(int[] intList)throws RemoteException;
    
    public double celsiusToFahrenheit(double celcius)throws RemoteException;
    
    public double fahrenheitToCelsius(double fahrenheit)throws RemoteException;
    
    public String getCurrentDate()throws RemoteException;
    
    public String getCurrentTime()throws RemoteException;
    
    public byte[] downloadFile(String fileName)throws RemoteException;
    
    public String[] getAllFilesFromBlob()throws RemoteException;
}  