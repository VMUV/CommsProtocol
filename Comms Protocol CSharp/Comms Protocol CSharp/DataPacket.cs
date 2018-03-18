
namespace Comms_Protocol_CSharp
{
    public class DataPacket
    {
        public const short NumOverHeadBytes = 3;
        private const int _typePos = 0;
        private const int _lenPos1 = 1;
        private const int _lenPos2 = 2;
        private const int _dataStartPos = 3;

        private byte[] _payload = new byte[0];
        private ValidPacketTypes _type = ValidPacketTypes.end_valid_packet_types;
        private short _expectedLen = -1;
        public byte[] Payload { get; set; }
        public ValidPacketTypes Type { get; set; }
        public short ExpectedLen { get; set; }

        public byte[] DeBuffer()
        {
            if (_expectedLen == -1)
                return null;

            byte[] buffer = new byte[NumOverHeadBytes + _expectedLen];
            buffer[_typePos] = (byte)_type;
            SerializeUtilities.BufferInt16InToByteArray(_expectedLen, buffer, 
                _lenPos1, Endianness.big_endian);
            for (int i = 0; i < _expectedLen; i++)
                buffer[_dataStartPos + i] = _payload[i];
            return buffer;
        }
    }

    public enum ValidPacketTypes
    {
        test_packet = 0,
        motus_1_raw_data_packet = 1,
        end_valid_packet_types
    }
}
