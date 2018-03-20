package comms.protocol.java;

public class SerializeUtilities 
{
    public static byte[] ConvertShortToByteArray(short i, Endianness e)
    {
        if (i > Short.MAX_VALUE)
            i = Short.MAX_VALUE;
        else if (i < Short.MIN_VALUE)
            i = Short.MIN_VALUE;

        byte[] rtn;
        if (e == Endianness.little_endian)
            rtn = new byte[] { (byte)(i & 0xFF), (byte)((i >> 8) & 0xFF) };
        else
            rtn = new byte[] { (byte)((i >> 8) & 0xFF), (byte)(i & 0xFF) };

        return rtn;
    }

    public static int BufferShortInToByteArray(short i, byte[] array, int indexToInsertElement, Endianness e)
    {
        try
        {
            byte[] vals = ConvertShortToByteArray(i, e);
            array[indexToInsertElement] = vals[0];
            indexToInsertElement++;
            array[indexToInsertElement] = vals[1];
            indexToInsertElement++;
        }
        catch (IndexOutOfBoundsException e1) { }
        return indexToInsertElement;
    }

    public static short ConvertByteArrayToShort(byte[] array, Endianness e)
    {
        short rtn;
        try
        {
            if (e == Endianness.little_endian)
            {
                rtn = (short)(array[1] & 0xFF);
                rtn <<= 8;
                rtn |= (short)(array[0] & 0xFF);
            }
            else
            {
                rtn = (short)(array[0] & 0xFF);
                rtn <<= 8;
                rtn |= (short)(array[1] & 0xFF);
            }
        }
        catch (IndexOutOfBoundsException e2)
        {
            rtn = 0;
        }
        return rtn;
    }
    
    public static byte[] ConvertFloatToByteArray(float f, Endianness e)
    {
    	byte[] rtn = new byte[4];
    	int intRepresentation = Float.floatToRawIntBits(f);
    	if (e == Endianness.little_endian)
    	{
	    	rtn[0] = (byte)(intRepresentation & 0xFF);
	    	rtn[1] = (byte)((intRepresentation >> 8) & 0xFF);
	    	rtn[2] = (byte)((intRepresentation >> 16) & 0xFF);
	    	rtn[3] = (byte)((intRepresentation >> 24) & 0xFF);
    	}
    	else
    	{
    		rtn[3] = (byte)(intRepresentation & 0xFF);
	    	rtn[2] = (byte)((intRepresentation >> 8) & 0xFF);
	    	rtn[1] = (byte)((intRepresentation >> 16) & 0xFF);
	    	rtn[0] = (byte)((intRepresentation >> 24) & 0xFF);
    	}
    	return rtn;
    }
    
    public static int BufferFloatInToByteArray(float f, byte[] array, int indexToInsertElement, Endianness e)
    {
        try
        {
            byte[] vals = ConvertFloatToByteArray(f, e);
            array[indexToInsertElement] = vals[0];
            indexToInsertElement++;
            array[indexToInsertElement] = vals[1];
            indexToInsertElement++;
            array[indexToInsertElement] = vals[2];
            indexToInsertElement++;
            array[indexToInsertElement] = vals[3];
            indexToInsertElement++;
        }
        catch (IndexOutOfBoundsException e1) { }
        return indexToInsertElement;
    }
    
    public static float ConvertByteArrayToFloat(byte[] array, Endianness e)
    {
    	float rtn;
    	int tempVal;
        try
        {
            if (e == Endianness.little_endian)
            {
                tempVal = (int)(array[3] & 0xFF);
                tempVal <<= 8;
                tempVal |= (int)(array[2] & 0xFF);
                tempVal <<= 8;
                tempVal |= (int)(array[1] & 0xFF);
                tempVal <<= 8;
                tempVal |= (int)(array[0] & 0xFF);
            }
            else
            {
                tempVal = (int)(array[0] & 0xFF);
                tempVal <<= 8;
                tempVal |= (int)(array[1] & 0xFF);
                tempVal <<= 8;
                tempVal |= (int)(array[2] & 0xFF);
                tempVal <<= 8;
                tempVal |= (int)(array[3] & 0xFF);
            }
            rtn = Float.intBitsToFloat(tempVal);
        }
        catch (IndexOutOfBoundsException e2)
        {
            rtn = 0;
        }
        return rtn;
    }
}
