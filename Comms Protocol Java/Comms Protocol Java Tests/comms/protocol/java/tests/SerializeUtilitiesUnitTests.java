package comms.protocol.java.tests;
import org.junit.Test;

import comms.protocol.java.Endianness;
import comms.protocol.java.SerializeUtilities;

import static org.junit.Assert.assertEquals;

public class SerializeUtilitiesUnitTests 
{
	//tests to ensure the (short to byte array) conversion is ordering the bytes correctly
	@Test
    public void ConvertShortToByteArrayTestEndianness()
    {
        short testVal = 0x6985;
        byte[] bigEndian =
            SerializeUtilities.ConvertShortToByteArray(testVal, Endianness.big_endian);
        byte[] littleEndian =
            SerializeUtilities.ConvertShortToByteArray(testVal, Endianness.little_endian);
        assertEquals((byte)((testVal >> 8) & 0xFF), bigEndian[0]);
        assertEquals((byte)(testVal & 0xFF), bigEndian[1]);
        assertEquals((byte)(testVal & 0xFF), littleEndian[0]);
        assertEquals((byte)((testVal >> 8) & 0xFF), littleEndian[1]);
    }
	
	//tests to ensure our try catch block for array out of bounds is working
	//will fill the first two slots in the buffer, then terminate out of the try block for the second two attempted insertions
	@Test
	public void BufferShortInToByteArrayBufferOverrunTest()
	{
		short testVal = 0x1234;
        byte[] testBuff = new byte[2];
        int i = 0;
        i = SerializeUtilities.BufferShortInToByteArray(testVal,
            testBuff, i, Endianness.big_endian);
        i = SerializeUtilities.BufferShortInToByteArray(testVal,
            testBuff, i, Endianness.big_endian);
        i = SerializeUtilities.BufferShortInToByteArray(testVal,
            testBuff, i, Endianness.big_endian);
        assertEquals(2, i);

        i = 0;
        i = SerializeUtilities.BufferShortInToByteArray(testVal,
            testBuff, i, Endianness.little_endian);
        i = SerializeUtilities.BufferShortInToByteArray(testVal,
            testBuff, i, Endianness.little_endian);
        i = SerializeUtilities.BufferShortInToByteArray(testVal,
            testBuff, i, Endianness.little_endian);
        assertEquals(2, i);
	}
	
	//Test that the (short to byte array) conversion will correctly be buffered into an array for both big and little endian formats
	@Test
	public void BufferShortInToByteArrayTest()
	{
		short testVal = 0x3456;
        byte[] testBuff = new byte[10];
        for (int i = 0; i < testBuff.length;)
        {
            i = SerializeUtilities.BufferShortInToByteArray(testVal,
                testBuff, i, Endianness.big_endian);
        }

        for (int i = 0; i < testBuff.length;)
        {
        	assertEquals((byte)((testVal >> 8) & 0xFF), testBuff[i++]);
        	assertEquals((byte)(testVal & 0xFF), testBuff[i++]);
        }

        for (int i = 0; i < testBuff.length;)
        {
            i = SerializeUtilities.BufferShortInToByteArray(testVal,
                testBuff, i, Endianness.little_endian);
        }

        for (int i = 0; i < testBuff.length;)
        {
        	assertEquals((byte)(testVal & 0xFF), testBuff[i++]);
        	assertEquals((byte)((testVal >> 8) & 0xFF), testBuff[i++]);
        }
    }
	
	//first, tests that the (short to byte array) conversion of the known value is valid for both big and little endian
	//next, tests that the conversion of the big and little endian ordered byte arrays are correctly converted back to shorts by checking them against the known value
	@Test
    public void ConvertByteArrayToShortTest()
    {
        short knownVal = 0x1234;
        byte[] littleEndian =
            SerializeUtilities.ConvertShortToByteArray(knownVal, Endianness.little_endian);
        byte[] bigEndian =
            SerializeUtilities.ConvertShortToByteArray(knownVal, Endianness.big_endian);
        short littleTest =
            SerializeUtilities.ConvertByteArrayToShort(littleEndian, Endianness.little_endian);
        short bigTest =
            SerializeUtilities.ConvertByteArrayToShort(bigEndian, Endianness.big_endian);

        assertEquals((byte)((knownVal >> 8) & 0xFF), bigEndian[0]);
        assertEquals((byte)(knownVal & 0xFF), bigEndian[1]);

        assertEquals((byte)((knownVal >> 8) & 0xFF), littleEndian[1]);
        assertEquals((byte)(knownVal & 0xFF), littleEndian[0]);
        
        assertEquals(knownVal, littleTest);
        assertEquals(knownVal, bigTest);
    }
	
	//tests that our try catch block works correctly(returns 0 for byte arrays that have the wrong size: 0 or 1)
	@Test
    public void ConvertByteArrayToShortIllegalByteLen()
    {
        byte[] array = new byte[1];
        short testval =
            SerializeUtilities.ConvertByteArrayToShort(array, Endianness.big_endian);
        assertEquals(0, testval);

        testval =
            SerializeUtilities.ConvertByteArrayToShort(array, Endianness.little_endian);
        assertEquals(0, testval);
    }
	
	

}
