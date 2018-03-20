using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Comms_Protocol_CSharp;

namespace Comms_Protocol_CSharp_Tests
{
    [TestClass]
    public class DataPacketUnitTests
    {
        short testPayloadLen = 0x0005;
        byte[] streamTest = new byte[] { DataPacket.Header1, DataPacket.Header2, (byte)ValidPacketTypes.test_packet, (byte)(0x0005 >> 8),
                                    (byte)(0x0005), 0, 1, 2, 3, 4 };
        [TestMethod]
        public void DataPacket_TestConstructor()
        {
            DataPacket defaultConstr = new DataPacket();
            byte[] test = new byte[] { 0, 1, 2, 3, 4 };
            DataPacket constr = new DataPacket(ValidPacketTypes.test_packet, (short)test.Length, test);

            Assert.AreEqual(ValidPacketTypes.end_valid_packet_types, defaultConstr.Type);
            Assert.AreEqual(-1, defaultConstr.ExpectedLen);

            Assert.AreEqual(ValidPacketTypes.test_packet, constr.Type);
            Assert.AreEqual(test.Length, constr.ExpectedLen);
            for (int i = 0; i < constr.ExpectedLen; i++)
                Assert.AreEqual(constr.Payload[i], test[i]);
        }

        [TestMethod]
        public void DataPacket_TestSerializeFromStream()
        {
            DataPacket packet = new DataPacket();
            int index = packet.SerializeFromStream(streamTest, 0);
            Assert.AreEqual(streamTest.Length, index);
            Assert.AreEqual(ValidPacketTypes.test_packet, packet.Type);
            Assert.AreEqual(testPayloadLen, packet.ExpectedLen);
            for (int i = 0; i < packet.ExpectedLen; i++)
                Assert.AreEqual(packet.Payload[i], streamTest[DataPacket.DataStartPos + i]);
        }

        [TestMethod]
        public void DataPacket_TestSerializeFromStreamBadHeader()
        {
            DataPacket packet = new DataPacket();
            int offset = 1;
            offset = packet.SerializeFromStream(streamTest, offset);
            Assert.AreEqual(2, offset);
            Assert.AreEqual(ValidPacketTypes.end_valid_packet_types, packet.Type);
            Assert.AreEqual(-1, packet.ExpectedLen);
        }

        [TestMethod]
        public void DataPacket_TestSerializeFromStreamBadLen()
        {
            byte[] badLen = new byte[] { DataPacket.Header1, DataPacket.Header2, (byte)ValidPacketTypes.test_packet, (byte)(0x0005 >> 8),
                                    (byte)(0x0005), 0, 1, 2, 3 };
            DataPacket packet = new DataPacket();
            int offset = 0;
            offset = packet.SerializeFromStream(badLen, offset);
            Assert.AreEqual(5, offset);
            Assert.AreEqual(ValidPacketTypes.end_valid_packet_types, packet.Type);
            Assert.AreEqual(-1, packet.ExpectedLen);
        }

        [TestMethod]
        public void DataPacket_TestSerializeFromStreamBadType()
        {
            byte[] badType = new byte[] { DataPacket.Header1, DataPacket.Header2, (byte)(ValidPacketTypes.end_valid_packet_types + 1), (byte)(0x0005 >> 8),
                                    (byte)(0x0005), 0, 1, 2, 3, 4 };
            DataPacket packet = new DataPacket();
            int offset = 0;
            offset = packet.SerializeFromStream(badType, offset);
            Assert.AreEqual(3, offset);
            Assert.AreEqual(ValidPacketTypes.end_valid_packet_types, packet.Type);
            Assert.AreEqual(-1, packet.ExpectedLen);
        }

        [TestMethod]
        public void DataPacket_TestSerializeToStream()
        {
            byte[] stream = new byte[64];
            byte[] test = new byte[] { 0, 1, 2, 3, 4 };
            DataPacket packet = new DataPacket(ValidPacketTypes.test_packet, (short)test.Length, test);
            int numBytesQueued = packet.SerializeToStream(stream, 0);
            Assert.AreEqual(streamTest.Length, numBytesQueued);
            Assert.AreEqual(ValidPacketTypes.test_packet, packet.Type);
            Assert.AreEqual(test.Length, packet.ExpectedLen);
            for (int i = 0; i < packet.ExpectedLen; i++)
                Assert.AreEqual(packet.Payload[i], streamTest[DataPacket.DataStartPos + i]);
        }

        [TestMethod]
        public void DataPacket_TestSerializeToStreamBadType()
        {
            byte[] stream = new byte[64];
            byte[] test = new byte[] { 0, 1, 2, 3, 4 };
            DataPacket packet = new DataPacket(ValidPacketTypes.end_valid_packet_types, (short)test.Length, test);
            int numBytesQueued = packet.SerializeToStream(stream, 0);
            Assert.AreEqual(0, numBytesQueued);
            Assert.AreEqual(ValidPacketTypes.end_valid_packet_types, packet.Type);
            Assert.AreEqual((short)test.Length, packet.ExpectedLen);
        }

        [TestMethod]
        public void DataPacket_TestSerializeToStreamBufferOverRun()
        {
            byte[] stream = new byte[1];
            byte[] test = new byte[] { 0, 1, 2, 3, 4 };
            DataPacket packet = new DataPacket(ValidPacketTypes.test_packet, (short)test.Length, test);
            int numBytesQueued = packet.SerializeToStream(stream, 0);
            Assert.AreEqual(0, numBytesQueued);
            Assert.AreEqual(ValidPacketTypes.test_packet, packet.Type);
            Assert.AreEqual((short)test.Length, packet.ExpectedLen);
        }
    }
}
