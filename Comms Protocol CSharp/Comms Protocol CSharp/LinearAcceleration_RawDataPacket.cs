
namespace Comms_Protocol_CSharp
{
    public class LinearAcceleration_RawDataPacket : AndroidDataPacket
    {
        private static int numberOfVals = 3;

        public LinearAcceleration_RawDataPacket() : base(ValidPacketTypes.linear_acceleration_raw_data_packet, numberOfVals)
        {

        }

        public LinearAcceleration_RawDataPacket(byte[] payload) : base(ValidPacketTypes.linear_acceleration_raw_data_packet, numberOfVals, payload)
        {

        }

        public LinearAcceleration_RawDataPacket(DataPacket packet) : base(ValidPacketTypes.linear_acceleration_raw_data_packet, numberOfVals, packet)
        {

        }

        public XYZSensor GetAcceleration()
        {
            AndroidSensor sensor = DeSerialize();
            return new XYZSensor(sensor.GetValues());
        }
    }
}
