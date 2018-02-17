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
                rtn = (short)(array[1]);
                rtn <<= 8;
                rtn |= (short)(array[0]);
            }
            else
            {
                rtn = (short)(array[0]);
                rtn <<= 8;
                rtn |= (short)(array[1]);
            }
        }
        catch (IndexOutOfBoundsException e2)
        {
            rtn = 0;
        }
        return rtn;
    }
}
