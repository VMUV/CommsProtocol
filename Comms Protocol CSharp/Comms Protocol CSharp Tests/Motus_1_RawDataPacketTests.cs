using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Comms_Protocol_CSharp;

namespace Comms_Protocol_CSharp_Tests
{
    [TestClass]
    public class Motus_1_RawDataPacketTests
    {
        byte[] testBytePayload = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 };
        Int16[] testIntPayload = new Int16[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };

        [TestMethod]
        public void Motus_1_PacketTestConstructor()
        {
            Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();
            Motus_1_RawDataPacket packet1 = new Motus_1_RawDataPacket(testBytePayload);
            DataPacket dPacket = new DataPacket(ValidPacketTypes.motus_1_raw_data_packet, 
                (short)testBytePayload.Length, testBytePayload);
            Motus_1_RawDataPacket packet2 = new Motus_1_RawDataPacket(dPacket);

            Assert.AreEqual(ValidPacketTypes.motus_1_raw_data_packet,
                packet.Type);
            Assert.AreEqual(18, packet.ExpectedLen);
            Assert.AreEqual(ValidPacketTypes.motus_1_raw_data_packet,
                packet1.Type);
            Assert.AreEqual(18, packet1.ExpectedLen);
            Assert.AreEqual(ValidPacketTypes.motus_1_raw_data_packet,
                            packet2.Type);
            Assert.AreEqual(18, packet2.ExpectedLen);

            for (int i = 0; i < testBytePayload.Length; i++)
            {
                Assert.AreEqual(testBytePayload[i], packet1.Payload[i]);
                Assert.AreEqual(testBytePayload[i], packet2.Payload[i]);
            }

        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Accepted illegal payload")]
        public void Motus_1_PacketTestInvalidConstructor()
        {
            byte[] invalidBytePayload = new byte[12];
            Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket(invalidBytePayload);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Accepted illegal payload")]
        public void Motus_1_PacketTestInvalidConstructor2()
        {
            byte[] invalidBytePayload = new byte[12];
            DataPacket dPacket = new DataPacket(ValidPacketTypes.motus_1_raw_data_packet, 
                (short)invalidBytePayload.Length, invalidBytePayload);

            Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket(dPacket);
            Assert.AreEqual(ValidPacketTypes.motus_1_raw_data_packet, packet.Type);
            Assert.AreEqual(18, packet.ExpectedLen);

            DataPacket dPacket1 = new DataPacket(ValidPacketTypes.motus_1_raw_data_packet,
                18, invalidBytePayload);
            Motus_1_RawDataPacket packet1 = new Motus_1_RawDataPacket(dPacket1);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Accepted illegal payload")]
        public void Motus_1_PacketTestSerializeInvalidPayload()
        {
            Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();
            byte[] invalidBytePayload = new byte[12];
            packet.Serialize(invalidBytePayload);
        }

        [TestMethod]
        public void Motus_1_PacketTestSerializeBytes()
        {
            Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();

            packet.Serialize(testBytePayload);
            byte[] rtnPayload = packet.Payload;
            Int16[] rtnIntPayload = packet.DeSerialize();

            Assert.AreEqual(packet.ExpectedLen, rtnPayload.Length);
            Assert.AreEqual(packet.ExpectedLen / 2, rtnIntPayload.Length);

            for (int i = 0; i < rtnPayload.Length; i++)
                Assert.AreEqual(testBytePayload[i], rtnPayload[i]);

            Int16[] known = new Int16[packet.ExpectedLen / 2];
            Buffer.BlockCopy(packet.Payload, 0, known, 0, packet.ExpectedLen);
            for (int i = 0; i < rtnIntPayload.Length; i++)
                Assert.AreEqual(known[i], rtnIntPayload[i]);
        }

        [TestMethod]
        public void Motus_1_PacketTestSerializeInts()
        {
            Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();

            packet.Serialize(testIntPayload);
            byte[] rtnPayload = packet.Payload;
            Int16[] rtnIntPayload = packet.DeSerialize();

            Assert.AreEqual(packet.ExpectedLen, rtnPayload.Length);
            Assert.AreEqual(packet.ExpectedLen / 2, rtnIntPayload.Length);

            byte[] known = new byte[packet.ExpectedLen];
            Buffer.BlockCopy(packet.Payload, 0, known, 0, packet.ExpectedLen);
            for (int i = 0; i < rtnPayload.Length; i++)
                Assert.AreEqual(known[i], rtnPayload[i]);

            for (int i = 0; i < rtnIntPayload.Length; i++)
                Assert.AreEqual(testIntPayload[i], rtnIntPayload[i]);
        }

        [TestMethod]
        public void Motus_1_TestPacketToString()
        {
            Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();

            packet.Serialize(testBytePayload);
            Int16[] intPayload = packet.DeSerialize();
            string known = intPayload[0].ToString();
            for (int i = 1; i < intPayload.Length; i++)
            {
                known += ",";
                known += intPayload[i].ToString();
            }

            Assert.AreEqual(known, packet.ToString());
        }
    }
}
