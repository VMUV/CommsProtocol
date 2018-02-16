package testingJavaCommsJarLib;

//showing how to import the vmuv comms protocol lib
import comms.protocol.java.*;

public class testMethods {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		byte[] buffer = new byte[6];
		int i = SerializeUtilities.BufferShortInToByteArray((short)0x6985, buffer, 0, Endianness.little_endian);
		System.out.println(i);
		
		//showing how to reference items in the lib
		Motus_1_RawDataPacket pack = new Motus_1_RawDataPacket();
	}

}
