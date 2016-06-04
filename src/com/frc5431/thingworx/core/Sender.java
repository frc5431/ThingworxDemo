package com.frc5431.thingworx.core;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Sender {
	
	private DatagramSocket client;
	private InetAddress ip;
	private int port;
	private final String
			PROP_PUT = "0x00",
			PROP_GET = "0xA1",
			PROP_MULTI = "<DATA-TYPE>",
			PROP_MULTI_EOF = "<EOF-TYPE>",
			EOF = "<STREAM:EOF>",
			SOF = "<STREAM:SOF>",
			ON_FAIL = "<STREAM:ERR>";
	
	public Sender(String ip, int port) throws Exception {
		client = new DatagramSocket();
		this.ip = InetAddress.getByName(ip);
		this.port = port;
	}
	
	private String sendRecv(String toSend) throws Exception{
		byte[] sendData = new byte[1024];
		byte[] recvData = new byte[1024];
		sendData = toSend.getBytes();
		DatagramPacket sender = new DatagramPacket(sendData, sendData.length, ip, port);
		client.send(sender);
		DatagramPacket recv = new DatagramPacket(recvData, recvData.length);
		client.receive(recv);
		String retState = new String(recv.getData());
		System.out.println("GOT " + retState);
		return retState;
	}
	
	public String put_property(String name, String value) {
		String type = "TEXT";
		if(name.length() < 1) type = "JSON"; 
		String compressed = SOF + PROP_PUT + name + PROP_MULTI + type + PROP_MULTI_EOF + value + EOF;
		String returned = "";
		try {
			returned = sendRecv(compressed);
		} catch(Throwable ignored) {}
		return returned.replace(SOF, "").replace(EOF, "");
	}

}
