namespace Comms_Protocol_CSharp
{
    class DataQueue
    {
        private int _bufferSize;
        private int _head = 0;
        private int _tail = 0;
        private byte[] _buffer;

        public int Index { get; }

        public DataQueue()
        {
            _bufferSize = 1024;
            _buffer = new byte[_bufferSize];
        }

        public DataQueue(int size)
        {
            _bufferSize = size;
            _buffer = new byte[_bufferSize];
        }

        public int GetBufferSpace()
        {
            return (_bufferSize - _head);
        }

        public void QueueDataPacket(DataPacket packet)
        {
            byte[] serial = packet.DeBuffer();
            short len = (short)(packet.ExpectedLen + DataPacket.NumOverHeadBytes);
            if ((serial != null) && (GetBufferSpace() > len))
            {
                for (int i = 0; i < serial.Length; i++)
                    _buffer[_head++] = serial[i];
            }
        }

        public void QueueBytes(byte[] bytes)
        {
            int len = bytes.Length;
            if (GetBufferSpace() > len)
            {
                for (int i = 0; i < len; i++)
                    _buffer[_head++] = bytes[i];
            }
        }

        public void Flush()
        {
            _head = 0;
            _tail = 0;
        }

        public byte[] GetAllBytes()
        {
            Flush();
            return _buffer;
        }

        public DataPacket GetDataPacket()
        {
            DataPacket packet = new DataPacket();
            if ((_head - _tail) > DataPacket.NumOverHeadBytes)
            {
                ValidPacketTypes type = (ValidPacketTypes)_buffer[_tail++];
                short len = (short)_buffer[_tail++];
                len <<= 8;
                len |= (short)_buffer[_tail++];
                if ((_head - _tail) >= len)
                {
                    byte[] payload = new byte[len];
                    for (int i = 0; i < len; i++)
                        payload[i] = _buffer[_tail++];
                    packet.Type = type;
                    packet.Payload = payload;
                }
            }

            return packet;
        }
    }
}
