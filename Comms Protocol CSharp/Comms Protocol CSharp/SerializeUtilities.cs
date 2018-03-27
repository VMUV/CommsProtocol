using System;

namespace Comms_Protocol_CSharp
{
    public static class SerializeUtilities
    {
        public static byte[] ConvertInt16ToByteArray(Int16 i, Endianness e)
        {
            if (i > Int16.MaxValue)
                i = Int16.MaxValue;
            else if (i < Int16.MinValue)
                i = Int16.MinValue;

            byte[] rtn;
            if (e == Endianness.little_endian)
                rtn = new byte[] { (byte)(i & 0xFF), (byte)((i >> 8) & 0xFF) };
            else
                rtn = new byte[] { (byte)((i >> 8) & 0xFF), (byte)(i & 0xFF) };

            return rtn;
        }

        public static int BufferInt16InToByteArray(Int16 i, byte[] array, int indexToInsertElement, Endianness e)
        {
            try
            {
                byte[] vals = ConvertInt16ToByteArray(i, e);
                array[indexToInsertElement] = vals[0];
                indexToInsertElement++;
                array[indexToInsertElement] = vals[1];
                indexToInsertElement++;
            }
            catch (IndexOutOfRangeException) { }
            return indexToInsertElement;
        }

        public static Int16 ConvertByteArrayToInt16(byte[] array, Endianness e)
        {
            Int16 rtn = new Int16();
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
            catch (IndexOutOfRangeException)
            {
                rtn = 0;
            }
            return rtn;
        }

        public static byte[] ConvertFloatToByteArray(float f)
        {
            byte[] rtn = new byte[4];
            float[] fArr = new float[1];
            fArr[0] = f;

            try
            {
                Buffer.BlockCopy(fArr, 0, rtn, 0, 4);
            }
            catch (Exception) { };

            return rtn;
        }

        public static int BufferFloatInToByteArray(float f, byte[] array, int indexToInsertElement)
        {
            byte[] bytes = ConvertFloatToByteArray(f);
            try
            {
                Buffer.BlockCopy(bytes, 0, array, indexToInsertElement, 4);
                indexToInsertElement += 4;
            }
            catch (Exception) { }
            return indexToInsertElement;
        }

        public static float ConvertByteArrayToFloat(byte[] array)
        {
            float[] fArr = new float[1];

            try
            {
                Buffer.BlockCopy(array, 0, fArr, 0, 4);
            }
            catch (Exception) { };

            return fArr[0];
        }
    }

    public enum Endianness
    {
        little_endian = 0,
        big_endian = 1
    }
}
