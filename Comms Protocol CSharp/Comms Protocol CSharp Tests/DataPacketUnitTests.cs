using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Comms_Protocol_CSharp;

namespace Comms_Protocol_CSharp_Tests
{
    [TestClass]
    public class DataPacketUnitTests
    {
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
        public void DataPacket_TestSerialize()
        {
            DataPacket packet = new DataPacket();
            short testPayloadLen = 5;
            byte[] stream = new byte[] { (byte)ValidPacketTypes.test_packet, (byte)(testPayloadLen >> 8),
                                    (byte)(testPayloadLen), 0, 1, 2, 3, 4 };

            packet.Serialize(stream, 0);
            Assert.AreEqual(ValidPacketTypes.test_packet, packet.Type);
            Assert.AreEqual(testPayloadLen, packet.ExpectedLen);
            for (int i = 0; i < packet.ExpectedLen; i++)
                Assert.AreEqual(packet.Payload[i], stream[DataPacket.DataStartPos + i]);
        }
    }
}
