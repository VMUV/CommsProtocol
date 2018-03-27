using System;

namespace Comms_Protocol_CSharp
{
    public class RotationVectorRawDataPacket : DataPacket
    {

        private const int NUM_ELMTS_IN_QUAT = 4;
        private const int NUM_BYTES_IN_FLOAT = 4;

        public RotationVectorRawDataPacket()
        {
            this.Type = ValidPacketTypes.rotation_vector_raw_data_packet;
            this.ExpectedLen = 16;
            this.Payload = new byte[0];
        }

        public RotationVectorRawDataPacket(byte[] payload)
        {
            this.Type = ValidPacketTypes.rotation_vector_raw_data_packet;
            this.ExpectedLen = 16;
            this.Serialize(payload);
        }

        public RotationVectorRawDataPacket(DataPacket packet)
        {
            if ((packet.Type != ValidPacketTypes.rotation_vector_raw_data_packet) ||
                (packet.ExpectedLen != 16))
            {
                this.Type = ValidPacketTypes.rotation_vector_raw_data_packet;
                this.ExpectedLen = 16;
                this.Payload = new byte[0];
            }
            else
            {
                this.Type = packet.Type;
                this.ExpectedLen = packet.ExpectedLen;
                this.Serialize(packet.Payload);
            }
        }

        public void Serialize(byte[] payload)
        {
            if (payload.Length < 16)
                throw new Exception();

            this.Payload = payload;
        }

        public void Serialize(float[] rawSensorData)
        {
            if (rawSensorData.Length < NUM_ELMTS_IN_QUAT)
                throw new Exception();
            byte[] payload = new byte[NUM_ELMTS_IN_QUAT * NUM_BYTES_IN_FLOAT];
            int indexToInsertElement = 0;
            for (int i = 0; i < NUM_ELMTS_IN_QUAT; i++)
            {
                indexToInsertElement = SerializeUtilities.BufferFloatInToByteArray(
                    rawSensorData[i], payload, indexToInsertElement);
            }

            this.Payload = payload;
        }

        public float[] DeSerialize()
        {
            float[] rtn = new float[4];
            int byteIndex = 0;
            byte[] bytePayload = this.Payload;
            try
            {
                for (int i = 0; i < rtn.Length; i++)
                {
                    byte[] tmp = new byte[NUM_BYTES_IN_FLOAT];
                    tmp[0] = bytePayload[byteIndex++];
                    tmp[1] = bytePayload[byteIndex++];
                    tmp[2] = bytePayload[byteIndex++];
                    tmp[3] = bytePayload[byteIndex++];
                    rtn[i] = SerializeUtilities.ConvertByteArrayToFloat(tmp);
                }
            }
            catch (Exception) { }
            return rtn;
        }
    }
}
