package application;

import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.*;
import net.wimpi.modbus.*;
import net.wimpi.modbus.msg.*;
import net.wimpi.modbus.io.*;
import net.wimpi.modbus.net.*;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleRegister;
import net.wimpi.modbus.util.*;

public class KpnModbusUpload implements Runnable {
	private Register[] regs;
	private InetAddress address;
	private TCPMasterConnection connection;
	
	private Label connectionInfo;
	private ProgressBar progressBar;
	
	/**
	 * @param connectionInfo the connectionInfo to set
	 */
	public void setConnectionInfo(Label connectionInfo) {
		this.connectionInfo = connectionInfo;
	}

	/**
	 * @param progressBar the progressBar to set
	 */
	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public boolean connect()
	{
		try {
			connection.connect();
		} catch(Exception e) 
		{
			e.printStackTrace();
		}
		
		System.out.println("Modbus connected: " + connection.isConnected());
		return connection.isConnected();
	}
	
	public KpnModbusUpload(String slaveIpAddress) throws Exception
	{
		address = InetAddress.getByName(slaveIpAddress); //("192.168.153.101");
		connection = new TCPMasterConnection(address);
		connection.setPort(502);		
		//connection.connect();
		//System.out.println("Modbus connected: " + connection.isConnected());

		ArrayList<Integer> arrlist = new ArrayList<>();
		arrlist.add(10);
		arrlist.add(20);
		arrlist.add(30);
		arrlist.add(40);
		WriteMultipleRegistersRequest request = new WriteMultipleRegistersRequest(228, makeRegisters(arrlist));
		
		ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
		transaction.setRequest(request);
		transaction.execute();
		System.out.println(transaction.getResponse().getHexMessage());
	}
		
	public Register[] makeRegisters(ArrayList<Integer> rawdata)
	{
			regs = new Register[rawdata.size()];
			int i=0;
			for(Integer data : rawdata)
			{
				regs[i++] = new SimpleRegister(data);
			}
			return regs;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
