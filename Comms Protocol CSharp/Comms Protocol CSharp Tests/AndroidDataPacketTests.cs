using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Comms_Protocol_CSharp;

namespace Comms_Protocol_CSharp_Tests
{
    [TestClass]
    public class AndroidDataPacketTests
    {
        private static byte[] dummyBytePayload = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 };
        private static ValidPacketTypes dummyPacketType = ValidPacketTypes.accelerometer_raw_data_packet;
        private DataPacket dummyDataPacket = new DataPacket(dummyPacketType, (short)dummyBytePayload.Length,
                dummyBytePayload);
        private static float[] dummyFloatVals = { 1.0f, 2.0f, 3.0f };
        private static long dummyTimeStamp = 123456;
        private AndroidSensor dummySensor = new AndroidSensor(dummyFloatVals, dummyTimeStamp);

        [TestMethod]
        public void TestValidConstructor()
        {
            AndroidDataPacket packet1 = new AndroidDataPacket(ValidPacketTypes.accelerometer_raw_data_packet, 3);
            Assert.AreEqual(ValidPacketTypes.accelerometer_raw_data_packet, packet1.Type);
            Assert.AreEqual(20, packet1.ExpectedLen);

            try
            {
                AndroidDataPacket packet2 = new AndroidDataPacket(ValidPacketTypes.linear_acceleration_raw_data_packet, 3,
                        dummyBytePayload);
                Assert.AreEqual(ValidPacketTypes.linear_acceleration_raw_data_packet, packet2.Type);
                Assert.AreEqual(20, packet2.ExpectedLen);
                byte[] localPayload = packet2.Payload;
                for (int i = 0; i < packet2.ExpectedLen; i++)
                    Assert.AreEqual(dummyBytePayload[i], localPayload[i]);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                Assert.IsTrue(false);
            }

            try
            {
                AndroidDataPacket packet3 = new AndroidDataPacket(dummyPacketType, 3, dummyDataPacket);
                Assert.AreEqual(dummyPacketType, packet3.Type);
                Assert.AreEqual(20, packet3.ExpectedLen);
                byte[] localPayload = packet3.Payload;
                for (int i = 0; i < packet3.ExpectedLen; i++)
                    Assert.AreEqual(dummyBytePayload[i], localPayload[i]);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                Assert.IsTrue(false);
            }
        }

        [TestMethod]
        public void TestInvalidConstructor1()
        {
            byte[] badBytePayload = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };
            AndroidDataPacket packet2 = new AndroidDataPacket(ValidPacketTypes.linear_acceleration_raw_data_packet, 3, badBytePayload);
            Assert.AreEqual(20, packet2.ExpectedLen);
        }

        [TestMethod]
        public void TestInvalidConstructor2()
        {
            byte[] badBytePayload = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };
            ValidPacketTypes badPacketType = ValidPacketTypes.accelerometer_raw_data_packet;
            DataPacket badDataPacket = new DataPacket(badPacketType, (short)badBytePayload.Length, badBytePayload);
            AndroidDataPacket packet3 = new AndroidDataPacket(badPacketType, 3, badDataPacket);
            Assert.AreEqual(20, packet3.ExpectedLen);
        }

        [TestMethod]
        public void TestSerialize()
        {
            AndroidDataPacket packet1 = new AndroidDataPacket(dummyPacketType, 3);
            try
            {
                packet1.Serialize(dummyBytePayload);
                Assert.AreEqual(dummyPacketType, packet1.Type);
                Assert.AreEqual(dummyBytePayload.Length, packet1.ExpectedLen);
                byte[] localPayload = packet1.Payload;
                for (int i = 0; i < packet1.ExpectedLen; i++)
                    Assert.AreEqual(dummyBytePayload[i], localPayload[i]);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                Assert.IsTrue(false);
            }

            AndroidDataPacket packet2 = new AndroidDataPacket(dummyPacketType, 3);
            try
            {
                packet2.Serialize(dummySensor);
                Assert.AreEqual(dummyPacketType, packet2.Type);
                Assert.AreEqual(dummyBytePayload.Length, packet2.ExpectedLen);
                byte[] localPayload = packet2.Payload;
                byte[] localDummyPayload = dummySensor.GetBytes();
                for (int i = 0; i < packet2.ExpectedLen; i++)
                    Assert.AreEqual(localDummyPayload[i], localPayload[i]);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                Assert.IsTrue(false);
            }
        }
    }
}
