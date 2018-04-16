using System;

namespace Comms_Protocol_CSharp
{
    public class Motus_1_RawDataPacket : DataPacket
    {
        public Motus_1_RawDataPacket()
        {
            Type = ValidPacketTypes.motus_1_raw_data_packet;
            ExpectedLen = 18;
            Payload = new byte[ExpectedLen];
        }

        public Motus_1_RawDataPacket(byte[] payload)
        {
            Type = ValidPacketTypes.motus_1_raw_data_packet;
            ExpectedLen = 18;
            Serialize(payload);
        }

        public Motus_1_RawDataPacket(DataPacket packet)
        {
            if ((packet.Type != ValidPacketTypes.motus_1_raw_data_packet) ||
                (packet.ExpectedLen != 18))
            {
                Type = ValidPacketTypes.motus_1_raw_data_packet;
                ExpectedLen = 18;
                Payload = new byte[ExpectedLen];
            }
            else
            {
                Type = packet.Type;
                ExpectedLen = packet.ExpectedLen;
                Serialize(packet.Payload);
            }
        }

        public void Serialize(byte[] payload)
        {
            if (payload.Length != ExpectedLen)
                throw new ArgumentException();

            Payload = payload;
        }

        public void Serialize(Int16[] payload)
        {
            if (payload.Length != (ExpectedLen / 2))
                throw new ArgumentException();

            try
            {
                Buffer.BlockCopy(payload, 0, Payload, 0, ExpectedLen);
            }
            catch (Exception) { }
        }

        public Int16[] DeSerialize()
        {
            Int16[] rtn = new Int16[ExpectedLen / 2];
            try
            {
                if (Payload.Length == ExpectedLen)
                    Buffer.BlockCopy(Payload, 0, rtn, 0, ExpectedLen);
            }
            catch (Exception) { }
            return rtn;
        }

        public override string ToString()
        {
            string rtn = "";
            Int16[] vals = DeSerialize();
            if (vals.Length == ExpectedLen / 2)
            {
                rtn = vals[0].ToString();
                for (int i = 1; i < vals.Length; i++)
                {
                    rtn += ",";
                    rtn += vals[i].ToString();
                }
            }
            return rtn;
        }
    }
}
