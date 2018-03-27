using System;
using Comms_Protocol_CSharp;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace Comms_Protocol_CSharp_Tests
{
    [TestClass]
    public class RotationVectorRawDataPacketTests
    {
        byte[] testBytePayload = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        float[] testFloatPayload = new float[] { 0, 1, 2, 3 };

        [TestMethod]
        public void RotationVector_TestConstructor()
        {
            RotationVectorRawDataPacket packet = new RotationVectorRawDataPacket();
            RotationVectorRawDataPacket packet1 = new RotationVectorRawDataPacket(testBytePayload);
            DataPacket dPacket = new DataPacket(ValidPacketTypes.rotation_vector_raw_data_packet,
                (short)testBytePayload.Length, testBytePayload);
            RotationVectorRawDataPacket packet2 = new RotationVectorRawDataPacket(dPacket);

            Assert.AreEqual(ValidPacketTypes.rotation_vector_raw_data_packet,
                packet.Type);
            Assert.AreEqual(testBytePayload.Length, packet.ExpectedLen);
            Assert.AreEqual(ValidPacketTypes.rotation_vector_raw_data_packet,
                packet1.Type);
            Assert.AreEqual(testBytePayload.Length, packet1.ExpectedLen);
            Assert.AreEqual(ValidPacketTypes.rotation_vector_raw_data_packet,
                            packet2.Type);
            Assert.AreEqual(testBytePayload.Length, packet2.ExpectedLen);

            for (int i = 0; i < testBytePayload.Length; i++)
            {
                Assert.AreEqual(testBytePayload[i], packet1.Payload[i]);
                Assert.AreEqual(testBytePayload[i], packet2.Payload[i]);
            }

        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Accepted illegal payload")]
        public void RotationVector_TestInvalidConstructor()
        {
            byte[] invalidBytePayload = new byte[12];
            RotationVectorRawDataPacket packet = new RotationVectorRawDataPacket(invalidBytePayload);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Accepted illegal payload")]
        public void RotationVector_PacketTestInvalidConstructor2()
        {
            byte[] invalidBytePayload = new byte[12];
            DataPacket dPacket = new DataPacket(ValidPacketTypes.rotation_vector_raw_data_packet,
                (short)invalidBytePayload.Length, invalidBytePayload);

            RotationVectorRawDataPacket packet = new RotationVectorRawDataPacket(dPacket);
            Assert.AreEqual(ValidPacketTypes.rotation_vector_raw_data_packet, packet.Type);
            Assert.AreEqual(testBytePayload.Length, packet.ExpectedLen);

            DataPacket dPacket1 = new DataPacket(ValidPacketTypes.rotation_vector_raw_data_packet,
                (short)testBytePayload.Length, invalidBytePayload);
            RotationVectorRawDataPacket packet1 = new RotationVectorRawDataPacket(dPacket1);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Accepted illegal payload")]
        public void RotationVector_PacketTestSerializeInvalidPayload()
        {
            RotationVectorRawDataPacket packet = new RotationVectorRawDataPacket();
            byte[] invalidBytePayload = new byte[12];
            packet.Serialize(invalidBytePayload);
        }

        [TestMethod]
        public void RotationVector_PacketTestSerializeBytes()
        {
            RotationVectorRawDataPacket packet = new RotationVectorRawDataPacket();

            packet.Serialize(testBytePayload);
            byte[] rtnPayload = packet.Payload;
            float[] rtnFloatPayload = packet.DeSerialize();

            Assert.AreEqual(packet.ExpectedLen, rtnPayload.Length);
            Assert.AreEqual(packet.ExpectedLen / 4, rtnFloatPayload.Length);

            for (int i = 0; i < rtnPayload.Length; i++)
                Assert.AreEqual(testBytePayload[i], rtnPayload[i]);

            float[] known = new float[packet.ExpectedLen / 4];
            Buffer.BlockCopy(packet.Payload, 0, known, 0, packet.ExpectedLen);
            for (int i = 0; i < rtnFloatPayload.Length; i++)
                Assert.AreEqual(known[i], rtnFloatPayload[i]);
        }

        [TestMethod]
        public void RotationVector_PacketTestSerializeInts()
        {
            RotationVectorRawDataPacket packet = new RotationVectorRawDataPacket();

            packet.Serialize(testFloatPayload);
            byte[] rtnPayload = packet.Payload;
            float[] rtnFloatPayload = packet.DeSerialize();

            Assert.AreEqual(packet.ExpectedLen, rtnPayload.Length);
            Assert.AreEqual(packet.ExpectedLen / 4, rtnFloatPayload.Length);

            byte[] known = new byte[packet.ExpectedLen];
            Buffer.BlockCopy(packet.Payload, 0, known, 0, packet.ExpectedLen);
            for (int i = 0; i < rtnPayload.Length; i++)
                Assert.AreEqual(known[i], rtnPayload[i]);

            for (int i = 0; i < rtnFloatPayload.Length; i++)
                Assert.AreEqual(testFloatPayload[i], rtnFloatPayload[i]);
        }

        [TestMethod]
        public void RotationVector_TestPacketToString()
        {
            RotationVectorRawDataPacket packet = new RotationVectorRawDataPacket();

            packet.Serialize(testBytePayload);
            float[] fltPayload = packet.DeSerialize();
            string known = fltPayload[0].ToString();
            for (int i = 1; i < fltPayload.Length; i++)
            {
                known += ",";
                known += fltPayload[i].ToString();
            }

            Assert.AreEqual(known, packet.ToString());
        }
    }
}
