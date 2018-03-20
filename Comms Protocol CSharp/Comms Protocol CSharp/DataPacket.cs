using System;

namespace Comms_Protocol_CSharp
{
    public class DataPacket
    {
        public const short NumOverHeadBytes = 5;
        public const int HeaderPos1 = 0;
        public const int HeaderPos2 = 1;
        public const int TypePos = 2;
        public const int LenPos1 = 3;
        public const int LenPos2 = 4;
        public const int DataStartPos = 5;
        public const byte Header1 = 0xFE;
        public const byte Header2 = 0x5A;

        private ValidPacketTypes _type;

        public byte[] Payload { get; set; }
        public ValidPacketTypes Type
        {
            get { return _type; }
            set
            {
                if (value > ValidPacketTypes.end_valid_packet_types)
                    _type = ValidPacketTypes.end_valid_packet_types;
                else
                    _type = value;
            }
        }
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

        public int SerializeToStream(byte[] stream, int streamOffset)
        {
            if (Type >= ValidPacketTypes.end_valid_packet_types)
                return streamOffset;

            if ((NumOverHeadBytes + ExpectedLen) > stream.Length)
                return streamOffset;

            int index = streamOffset;
            try
            {
                stream[index++] = Header1;
                stream[index++] = Header2;
                stream[index++] = (byte)Type;
                stream[index++] = (byte)(ExpectedLen >> 8);
                stream[index++] = (byte)(ExpectedLen);
                for (int i = 0; i < ExpectedLen; i++)
                    stream[index++] = Payload[i];
            }
            catch (IndexOutOfRangeException)
            {
                index--;
            }

            return index;
        }

        public int SerializeFromStream(byte[] stream, int streamOffset)
        {
            if ((streamOffset + NumOverHeadBytes) > stream.Length)
                return streamOffset;

            int index = streamOffset + HeaderPos1;
            byte header1 = stream[index++];
            if (Header1 != header1)
                return index;
            byte header2 = stream[index++];
            if (Header2 != header2)
                return --index;

            Type = (ValidPacketTypes)stream[index++];
            if (Type >= ValidPacketTypes.end_valid_packet_types)
                return index;

            ExpectedLen = (short)stream[index++];
            ExpectedLen <<= 8;
            ExpectedLen |= (short)stream[index++];

            if ((index + ExpectedLen) <= stream.Length)
            {
                Payload = new byte[ExpectedLen];
                for (int i = 0; i < ExpectedLen; i++)
                    Payload[i] = stream[index++];
            }
            else
            {
                Type = ValidPacketTypes.end_valid_packet_types;
                ExpectedLen = -1;
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
