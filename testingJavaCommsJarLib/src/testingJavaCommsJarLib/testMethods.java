package testingJavaCommsJarLib;

import comms.protocol.java.*;

public class testMethods {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		byte[] buffer = new byte[6];
		int i = SerializeUtilities.BufferShortInToByteArray((short)0x6985, buffer, 0, Endianness.little_endian);
		System.out.println(i);
	}

}
