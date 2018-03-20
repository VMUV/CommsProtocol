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

        public int Count => _fifo.Count;

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

        public int GetStreamable(byte[] stream)
        {
            int index = 0;
            int maxSize = stream.Length;
            while ((index < maxSize) && (_fifo.Count > 0))
            {
                DataPacket packet = Get();
                if ((index + packet.ExpectedLen + DataPacket.NumOverHeadBytes) < maxSize)
                    index = packet.SerializeToStream(stream, index);
                else
                {
                    Add(packet);
                    break;
                }
            }

            return index;
        }

        public void ParseStreamable(byte[] stream, int maxSize)
        {
            int index = 0;
            while ((index < maxSize) && (_fifo.Count < _maxSize))
            {
                DataPacket packet = new DataPacket();
                int newIndex = packet.SerializeFromStream(stream, index);

                if (newIndex > index)
                    index = newIndex;
                else
                    break;

                if (packet.Type < ValidPacketTypes.end_valid_packet_types)
                    Add(packet);
            }
        }
    }
}
