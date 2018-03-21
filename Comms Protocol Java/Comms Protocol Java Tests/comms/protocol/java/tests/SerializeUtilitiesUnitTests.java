package comms.protocol.java.tests;
import org.junit.Test;

import comms.protocol.java.Endianness;
import comms.protocol.java.SerializeUtilities;

import static org.junit.Assert.assertEquals;

public class SerializeUtilitiesUnitTests 
{
	/*Tests for short conversions*/
	
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
	
	/*Tests for float conversions*/
	
	@Test
    public void ConvertFloatToByteArrayTestEndianness()
    {
        float testVal = (float) .5;
        int testIntVal = Float.floatToIntBits(testVal);
        byte[] bigEndian =
            SerializeUtilities.ConvertFloatToByteArray(testVal, Endianness.big_endian);
        byte[] littleEndian =
            SerializeUtilities.ConvertFloatToByteArray(testVal, Endianness.little_endian);
        assertEquals((byte)((testIntVal >> 24) & 0xFF), bigEndian[0]);
        assertEquals((byte)((testIntVal >> 16) & 0xFF), bigEndian[1]);
        assertEquals((byte)((testIntVal >> 8) & 0xFF), bigEndian[2]);
        assertEquals((byte)(testIntVal & 0xFF), bigEndian[3]);
        assertEquals((byte)(testIntVal & 0xFF), littleEndian[0]);
        assertEquals((byte)((testIntVal >> 8) & 0xFF), littleEndian[1]);
        assertEquals((byte)((testIntVal >> 16) & 0xFF), littleEndian[2]);
        assertEquals((byte)((testIntVal >> 24) & 0xFF), littleEndian[3]);
    }
	
	@Test
	public void BufferFloatInToByteArrayBufferOverrunTest()
	{
		float testVal = (float) .5;
        byte[] testBuff = new byte[8];
        int i = 0;
        i = SerializeUtilities.BufferFloatInToByteArray(testVal,
            testBuff, i, Endianness.big_endian);
        i = SerializeUtilities.BufferFloatInToByteArray(testVal,
            testBuff, i, Endianness.big_endian);
        i = SerializeUtilities.BufferFloatInToByteArray(testVal,
            testBuff, i, Endianness.big_endian);
        assertEquals(8, i);

        i = 0;
        i = SerializeUtilities.BufferFloatInToByteArray(testVal,
            testBuff, i, Endianness.little_endian);
        i = SerializeUtilities.BufferFloatInToByteArray(testVal,
            testBuff, i, Endianness.little_endian);
        i = SerializeUtilities.BufferFloatInToByteArray(testVal,
            testBuff, i, Endianness.little_endian);
        assertEquals(8, i);
	}
	
	@Test
	public void BufferFloatInToByteArrayTest()
	{
		float testVal = (float) .5;
        int testIntVal = Float.floatToIntBits((float) .5);
        byte[] testBuff = new byte[12];
        for (int i = 0; i < testBuff.length;)
        {
            i = SerializeUtilities.BufferFloatInToByteArray(testVal,
                testBuff, i, Endianness.big_endian);
        }

        for (int i = 0; i < testBuff.length;)
        {
        	assertEquals((byte)((testIntVal >> 24) & 0xFF), testBuff[i++]);
        	assertEquals((byte)((testIntVal >> 16) & 0xFF), testBuff[i++]);
        	assertEquals((byte)((testIntVal >> 8) & 0xFF), testBuff[i++]);
        	assertEquals((byte)(testIntVal & 0xFF), testBuff[i++]);
        }

        for (int i = 0; i < testBuff.length;)
        {
            i = SerializeUtilities.BufferFloatInToByteArray(testVal,
                testBuff, i, Endianness.little_endian);
        }

        for (int i = 0; i < testBuff.length;)
        {
        	assertEquals((byte)(testIntVal & 0xFF), testBuff[i++]);
        	assertEquals((byte)((testIntVal >> 8) & 0xFF), testBuff[i++]);
        	assertEquals((byte)((testIntVal >> 16) & 0xFF), testBuff[i++]);
        	assertEquals((byte)((testIntVal >> 24) & 0xFF), testBuff[i++]);
        }
    }
	
	@Test
    public void ConvertByteArrayToFloatTest()
    {
		float knownVal = (float) .6;
        int knownIntVal = Float.floatToIntBits(knownVal);
        byte[] littleEndian =
            SerializeUtilities.ConvertFloatToByteArray(knownVal, Endianness.little_endian);
        byte[] bigEndian =
            SerializeUtilities.ConvertFloatToByteArray(knownVal, Endianness.big_endian);
        float littleTest =
            SerializeUtilities.ConvertByteArrayToFloat(littleEndian, Endianness.little_endian);
        float bigTest =
            SerializeUtilities.ConvertByteArrayToFloat(bigEndian, Endianness.big_endian);

        assertEquals((byte)((knownIntVal >> 24) & 0xFF), bigEndian[0]);
        assertEquals((byte)((knownIntVal >> 16) & 0xFF), bigEndian[1]);
        assertEquals((byte)((knownIntVal >> 8) & 0xFF), bigEndian[2]);
        assertEquals((byte)(knownIntVal & 0xFF), bigEndian[3]);

        assertEquals((byte)((knownIntVal >> 24) & 0xFF), littleEndian[3]);
        assertEquals((byte)((knownIntVal >> 16) & 0xFF), littleEndian[2]);
        assertEquals((byte)((knownIntVal >> 8) & 0xFF), littleEndian[1]);
        assertEquals((byte)(knownIntVal & 0xFF), littleEndian[0]);
        
        assertEquals(knownVal, littleTest, .001);
        assertEquals(knownVal, bigTest, .001);
    }
	
	@Test
    public void ConvertByteArrayToFloatIllegalByteLen()
    {
        byte[] array = new byte[1];
        float testval =
            SerializeUtilities.ConvertByteArrayToFloat(array, Endianness.big_endian);
        assertEquals(0, testval, .001);

        testval =
            SerializeUtilities.ConvertByteArrayToFloat(array, Endianness.little_endian);
        assertEquals(0, testval, .001);
    }
}
