using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Comms_Protocol_CSharp;

namespace Comms_Protocol_CSharp_Tests
{
    [TestClass]
    public class DataQueueUnitTests
    {
        private byte[] sampleMotusPayload = new byte[] { 0, 1, 2, 3, 4, 5, 6,
                                                    7, 8, 9, 10, 11, 12, 13,
                                                    14, 15, 16, 17};

        private void FillQueue(DataQueue queue, DataPacket packet)
        {
            for (int i = 0; i < queue.MaxSize; i++)
                queue.Add(packet);
        }

        [TestMethod]
        public void DataQueue_TestConstructor()
        {
            DataQueue defaultQueue = new DataQueue();
            DataQueue queue = new DataQueue(128);
            Assert.AreEqual(64, defaultQueue.MaxSize);
            Assert.AreEqual(128, queue.MaxSize);
        }

        [TestMethod]
        public void DataQueue_TestIsEmpty()
        {
            DataQueue queue = new DataQueue();
            Assert.IsTrue(queue.IsEmpty());
        }

        [TestMethod]
        public void DataQueue_TestAddPacket()
        {
            Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket(sampleMotusPayload);
            DataQueue queue = new DataQueue();

            Assert.IsTrue(queue.Add(packet));
            Assert.IsFalse(queue.IsEmpty());
        }

        [TestMethod]
        public void DataQueue_TestAddOverFlowProtection()
        {
            Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket(sampleMotusPayload);
            DataQueue queue = new DataQueue();

            for (int i = 0; i < queue.MaxSize; i++)
                Assert.IsTrue(queue.Add(packet));

            Assert.IsFalse(queue.Add(packet));
        }

        [TestMethod]
        public void DataQueue_TestGet()
        {
            Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket(sampleMotusPayload);
            DataQueue queue = new DataQueue();
            queue.Add(packet);

            DataPacket getPacket = queue.Get();
            Assert.AreEqual(packet.Type, getPacket.Type);
            Assert.AreEqual(packet.ExpectedLen, getPacket.ExpectedLen);
            for (int i = 0; i < packet.ExpectedLen; i++)
                Assert.AreEqual(packet.Payload[i], getPacket.Payload[i]);
        }

        [TestMethod]
        public void DataQueue_TestGetUnderFlowProtection()
        {
            Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket(sampleMotusPayload);
            DataQueue queue = new DataQueue();

            FillQueue(queue, packet);

            int count;
            for (count = 0; !queue.IsEmpty(); count++)
            {
                DataPacket getPacket = queue.Get();
                Assert.AreEqual(packet.Type, getPacket.Type);
                Assert.AreEqual(packet.ExpectedLen, getPacket.ExpectedLen);
                for (int j = 0; j < packet.ExpectedLen; j++)
                    Assert.AreEqual(packet.Payload[j], getPacket.Payload[j]);
            }

            Assert.AreEqual(count, queue.MaxSize);
            Assert.IsTrue(queue.IsEmpty());
            DataPacket badPacket = queue.Get();
            Assert.AreEqual(ValidPacketTypes.end_valid_packet_types, badPacket.Type);
            Assert.AreEqual(-1, badPacket.ExpectedLen);
        }

        [TestMethod]
        public void DataQueue_TestGetStreamable()
        {
            byte[] stream = new byte[2048];
            Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket(sampleMotusPayload);
            DataQueue queue = new DataQueue();
            int expectedNumBytesToQueue = ((18 + DataPacket.NumOverHeadBytes) * queue.MaxSize);

            FillQueue(queue, packet);
            int numBytesQueued = queue.GetStreamable(stream);
            Assert.AreEqual(expectedNumBytesToQueue, numBytesQueued);
            Assert.IsTrue(queue.IsEmpty());

            for (int i = 0; i < numBytesQueued; )
            {
                DataPacket rebuilt = new DataPacket();
                i += rebuilt.SerializeFromStream(stream, i);
                Assert.AreEqual(packet.Type, rebuilt.Type);
                Assert.AreEqual(packet.ExpectedLen, rebuilt.ExpectedLen);
                for (int j = 0; j < rebuilt.ExpectedLen; j++)
                    Assert.AreEqual(packet.Payload[j], rebuilt.Payload[j]);
            }
        }

        [TestMethod]
        public void DataQueue_TestParseStreamable()
        {
            byte[] stream = new byte[2048];
            Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket(sampleMotusPayload);
            DataQueue queue = new DataQueue();

            FillQueue(queue, packet);
            int numBytesQueued = queue.GetStreamable(stream);
            FillQueue(queue, packet);

            DataQueue queue2 = new DataQueue();
            queue2.ParseStreamable(stream, stream.Length);

            while (!queue2.IsEmpty() && !queue.IsEmpty())
            {
                DataPacket packet1 = queue.Get();
                DataPacket packet2 = queue2.Get();

                // Ensure packet1 is the same as packet
                Assert.AreEqual(packet.Type, packet1.Type);
                Assert.AreEqual(packet.ExpectedLen, packet1.ExpectedLen);
                for (int j = 0; j < packet1.ExpectedLen; j++)
                    Assert.AreEqual(packet.Payload[j], packet1.Payload[j]);

                // Ensure packet2 is the same as packet1
                Assert.AreEqual(packet1.Type, packet2.Type);
                Assert.AreEqual(packet1.ExpectedLen, packet2.ExpectedLen);
                for (int j = 0; j < packet2.ExpectedLen; j++)
                    Assert.AreEqual(packet1.Payload[j], packet2.Payload[j]);
            }
        }
    }
}
