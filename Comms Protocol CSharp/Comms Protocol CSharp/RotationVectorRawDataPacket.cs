
namespace Comms_Protocol_CSharp
{
    public class RotationVectorRawDataPacket : AndroidDataPacket
    {
        private static int numberOfVals = 4;

        public RotationVectorRawDataPacket() : base(ValidPacketTypes.rotation_vector_raw_data_packet, numberOfVals)
        {

        }

        public RotationVectorRawDataPacket(byte[] payload) : base(ValidPacketTypes.rotation_vector_raw_data_packet, numberOfVals, payload)
        {

        }

        public RotationVectorRawDataPacket(DataPacket packet) : base(ValidPacketTypes.rotation_vector_raw_data_packet, numberOfVals, packet)
        {

        }

        public Quaternion GetQuat()
        {
            AndroidSensor sensor = DeSerialize();
            return new Quaternion(sensor.GetValues());
        }
    }
}
