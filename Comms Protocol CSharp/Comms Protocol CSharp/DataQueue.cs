using System.Collections.Generic;

namespace Comms_Protocol_CSharp
{
    public class DataQueue
    {
        private Queue<DataPacket> _fifo;
        private int _maxSize;

        public DataQueue()
        {
            _maxSize = 64;
            _fifo = new Queue<DataPacket>(_maxSize);
        }

        public DataQueue(int size)
        {
            _maxSize = size;
            _fifo = new Queue<DataPacket>(_maxSize);
        }

        public int MaxSize => _maxSize;

        public bool IsEmpty()
        {
            return (_fifo.Count == 0);
        }

        public bool Add(DataPacket packet)
        {
            bool rtn = _fifo.Count < _maxSize;
            if (rtn)
                _fifo.Enqueue(packet);

            return rtn;
        }

        public DataPacket Get()
        {
            DataPacket rtn = new DataPacket();
            if (_fifo.Count > 0)
            {
                try { rtn = _fifo.Dequeue(); }
                catch (System.InvalidOperationException) { };
            }

            return rtn;
        }

        public int GetStreamable(byte[] stream, int maxSize)
        {
            int numBytesQueued = 0;
            while ((numBytesQueued < maxSize) && (_fifo.Count > 0))
            {
                DataPacket packet = Get();
                int size = packet.ExpectedLen;
                if (size == -1)
                    continue;

                size += DataPacket.NumOverHeadBytes;
                if ((maxSize - numBytesQueued) > size)
                {
                    stream[numBytesQueued++] = (byte)packet.Type;
                    stream[numBytesQueued++] = (byte)((packet.ExpectedLen) >> 8);
                    stream[numBytesQueued++] = (byte)(packet.ExpectedLen);
                    byte[] payload = packet.Payload;
                    for (int i = 0; i < packet.ExpectedLen; i++)
                        stream[numBytesQueued++] = payload[i];
                }
                else
                    break;
            }

            return numBytesQueued;
        }

        public void ParseStreamable(byte[] stream, int maxSize)
        {
            int index = 0;
            while ((index < maxSize) && (_fifo.Count < _maxSize))
            {
                ValidPacketTypes type;
                short expectedLen = 0;
                if ((maxSize - index) > DataPacket.NumOverHeadBytes)
                {
                    type = (ValidPacketTypes)stream[index++];
                    expectedLen = (short)stream[index++];
                    expectedLen <<= 8;
                    expectedLen |= (short)stream[index++];
                }
                else
                    return;

                if ((maxSize - index) > expectedLen)
                {
                    byte[] payload = new byte[expectedLen];
                    for (int i = 0; i < expectedLen; i++)
                        payload[i] = stream[index++];
                    DataPacket packet = new DataPacket();
                    packet.Type = type;
                    packet.ExpectedLen = expectedLen;
                    packet.Payload = payload;
                    Add(packet);
                }
            }
        }
    }
}
