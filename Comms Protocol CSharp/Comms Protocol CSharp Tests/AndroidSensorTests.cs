using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Comms_Protocol_CSharp;

namespace Comms_Protocol_CSharp_Tests
{
    [TestClass]
    public class AndroidSensorTests
    {
        private float[] dummyVals = { 0.35f, 0.669f, 45f, 22f, 6634.345f };
        private long dummyTimeStamp = 11216541;

        [TestMethod]
        public void AndroidSensor_TestConstructor()
        {
            AndroidSensor sensor1 = new AndroidSensor(10);
            Assert.AreEqual(10, sensor1.Length());
            Assert.AreEqual(0, sensor1.GetTimeStamp());

            float[] vals = { (float)7.6, (float)-45.0, (float)0.00025, 2 };
            long timeStamp = 1021964813;
            AndroidSensor sensor2 = new AndroidSensor(vals, timeStamp);
            float[] valsRtn = sensor2.GetValues();
            for (int i = 0; i < vals.Length; i++)
                Assert.AreEqual(vals[i], valsRtn[i]);
            Assert.AreEqual(timeStamp, sensor2.GetTimeStamp());
            Assert.AreEqual(vals.Length, sensor2.Length());
        }

        [TestMethod]
        public void AndroidSensor_TestValues()
        {
            AndroidSensor sensor1 = new AndroidSensor(dummyVals.Length);
            sensor1.SetValues(dummyVals);
            sensor1.SetTimeStamp(dummyTimeStamp);
            float[] vals = sensor1.GetValues();
            for (int i = 0; i < vals.Length; i++)
                Assert.AreEqual(dummyVals[i], vals[i]);
            Assert.AreEqual(dummyTimeStamp, sensor1.GetTimeStamp());
        }

        [TestMethod]
        public void AndroidSensor_TestBytes()
        {
            byte[] buffer = new byte[(dummyVals.Length * AndroidSensor.NUM_BYTES_PER_FLOAT) + AndroidSensor.NUM_BYTES_PER_LONG];
            Buffer.BlockCopy(dummyVals, 0, buffer, 0, (dummyVals.Length * AndroidSensor.NUM_BYTES_PER_FLOAT));
            long[] tmp = new long[] { dummyTimeStamp };
            Buffer.BlockCopy(tmp, 0, buffer, (dummyVals.Length * AndroidSensor.NUM_BYTES_PER_FLOAT), AndroidSensor.NUM_BYTES_PER_LONG);

            AndroidSensor sensor1 = new AndroidSensor(dummyVals.Length);
            sensor1.SetBytes(buffer);

            float[] vals = sensor1.GetValues();
            for (int i = 0; i < vals.Length; i++)
                Assert.AreEqual(dummyVals[i], vals[i]);
            Assert.AreEqual(dummyTimeStamp, sensor1.GetTimeStamp());
        }

        [TestMethod]
        public void AndroidSensor_TestBytesInvalid()
        {
            byte[] bytes = new byte[1];
            AndroidSensor sensor1 = new AndroidSensor(dummyVals.Length);
            sensor1.SetBytes(bytes);

            Assert.AreEqual(0, sensor1.GetTimeStamp());
        }
    }
}
