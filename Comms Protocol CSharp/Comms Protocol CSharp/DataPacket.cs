
namespace Comms_Protocol_CSharp
{
    public class DataPacket
    {
        public const short NumOverHeadBytes = 3;
        public const int TypePos = 0;
        public const int LenPos1 = 1;
        public const int LenPos2 = 2;
        public const int DataStartPos = 3;

        public byte[] Payload { get; set; }
        public ValidPacketTypes Type { get; set; }
        public short ExpectedLen { get; set; }

        public DataPacket()
        {
            Type = ValidPacketTypes.end_valid_packet_types;
            ExpectedLen = -1;
        }

        public DataPacket(ValidPacketTypes type, short len, byte[] payload)
        {
            Type = type;
            ExpectedLen = len;
            Payload = payload;
        }

        public int Serialize(byte[] stream, int streamOffset)
        {
            if ((streamOffset + NumOverHeadBytes) > stream.Length)
                return streamOffset;

            int index = streamOffset + TypePos;
            Type = (ValidPacketTypes)stream[index++];
            ExpectedLen = (short)stream[index++];
            ExpectedLen <<= 8;
            ExpectedLen |= (short)stream[index++];

            if ((index + ExpectedLen) <= stream.Length)
            {
                Payload = new byte[ExpectedLen];
                for (int i = 0; i < ExpectedLen; i++)
                    Payload[i] = stream[index++];
            }

            return index;
        }
    }

    public enum ValidPacketTypes
    {
        test_packet = 0,
        motus_1_raw_data_packet = 1,
        end_valid_packet_types
    }
}
