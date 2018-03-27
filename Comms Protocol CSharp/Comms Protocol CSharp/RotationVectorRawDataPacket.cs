using System;

namespace Comms_Protocol_CSharp
{
    public class RotationVectorRawDataPacket : DataPacket
    {

        private const int NUM_ELMTS_IN_QUAT = 4;
        private const int NUM_BYTES_IN_FLOAT = 4;

        public RotationVectorRawDataPacket()
        {
            Type = ValidPacketTypes.rotation_vector_raw_data_packet;
            ExpectedLen = NUM_ELMTS_IN_QUAT * NUM_BYTES_IN_FLOAT;
            Payload = new byte[ExpectedLen];
        }

        public RotationVectorRawDataPacket(byte[] payload)
        {
            Type = ValidPacketTypes.rotation_vector_raw_data_packet;
            ExpectedLen = NUM_ELMTS_IN_QUAT * NUM_BYTES_IN_FLOAT;
            Serialize(payload);
        }

        public RotationVectorRawDataPacket(DataPacket packet)
        {
            if ((packet.Type != ValidPacketTypes.rotation_vector_raw_data_packet) ||
                (packet.ExpectedLen != (NUM_ELMTS_IN_QUAT * NUM_BYTES_IN_FLOAT)))
            {
                Type = ValidPacketTypes.rotation_vector_raw_data_packet;
                ExpectedLen = NUM_ELMTS_IN_QUAT * NUM_BYTES_IN_FLOAT;
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
            if (payload.Length < ExpectedLen)
                throw new ArgumentException();

            Payload = payload;
        }

        public void Serialize(float[] rawSensorData)
        {
            if (rawSensorData.Length != NUM_ELMTS_IN_QUAT)
                throw new ArgumentException();

            try
            {
                Buffer.BlockCopy(rawSensorData, 0, Payload, 0, ExpectedLen);
            }
            catch (Exception) { }
        }

        public float[] DeSerialize()
        {
            float[] rtn = new float[4];
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
            float[] vals = DeSerialize();
            if (vals.Length == ExpectedLen / 4)
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
