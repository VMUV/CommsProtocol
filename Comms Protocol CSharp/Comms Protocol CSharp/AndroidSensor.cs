using System;

namespace Comms_Protocol_CSharp
{
    public class AndroidSensor
    {
        private float[] _values;
        private long _timeStamp;

        public static int NUM_BYTES_PER_LONG = 8;
        public static int NUM_BYTES_PER_FLOAT = 4;

        public AndroidSensor(int numValues)
        {
            _values = new float[numValues];
            _timeStamp = 0;
        }

        public AndroidSensor(float[] vals, long tStamp)
        {
            _values = vals;
            _timeStamp = tStamp;
        }

        public void SetValues(float[] vals)
        {
            _values = vals;
        }

        public void SetTimeStamp(long tStamp)
        {
            _timeStamp = tStamp;
        }

        public long GetTimeStamp()
        {
            return _timeStamp;
        }

        public float[] GetValues()
        {
            return _values;
        }

        public int Length()
        {
            return _values.Length;
        }

        public byte[] GetBytes()
        {
            int numBytesInVal = _values.Length * NUM_BYTES_PER_FLOAT;
            byte[] rtn = new byte[numBytesInVal + NUM_BYTES_PER_LONG];
            Buffer.BlockCopy(_values, 0, rtn, 0, numBytesInVal);
            long[] tmp = new long[] { _timeStamp };
            Buffer.BlockCopy(tmp, 0, rtn, numBytesInVal, NUM_BYTES_PER_LONG);
            return rtn;
        }

        public void SetBytes(byte[] bytes)
        {
            if (bytes.Length < ((_values.Length * NUM_BYTES_PER_FLOAT) + NUM_BYTES_PER_LONG))
                return;
            Buffer.BlockCopy(bytes, 0, _values, 0, (_values.Length * NUM_BYTES_PER_FLOAT));
            long[] tmp = new long[1];
            Buffer.BlockCopy(bytes, (_values.Length * NUM_BYTES_PER_FLOAT), tmp, 0, NUM_BYTES_PER_LONG);
            _timeStamp = tmp[0];
        }
    }
}
