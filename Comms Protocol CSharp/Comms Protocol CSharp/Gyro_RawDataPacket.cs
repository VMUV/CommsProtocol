
namespace Comms_Protocol_CSharp
{
    class Gyro_RawDataPacket : AndroidDataPacket
    {
        private static int numberOfVals = 3;

        public Gyro_RawDataPacket() : base(ValidPacketTypes.gyro_raw_data_packet, numberOfVals)
        {

        }

        public Gyro_RawDataPacket(byte[] payload) : base(ValidPacketTypes.gyro_raw_data_packet, numberOfVals, payload)
        {

        }

        public Gyro_RawDataPacket(DataPacket packet) : base(ValidPacketTypes.gyro_raw_data_packet, numberOfVals, packet)
        {

        }

        public XYZSensor GetAxisRotation()
        {
            AndroidSensor sensor = DeSerialize();
            return new XYZSensor(sensor.GetValues());
        }
    }
}
