
namespace Comms_Protocol_CSharp
{
    public class Accelerometer_RawDataPacket : AndroidDataPacket
    {
        private static int numberOfVals = 3;

        public Accelerometer_RawDataPacket() : base(ValidPacketTypes.accelerometer_raw_data_packet, numberOfVals)
        {

        }

        public Accelerometer_RawDataPacket(byte[] payload) : base(ValidPacketTypes.accelerometer_raw_data_packet, numberOfVals, payload)
        {

        }

        public Accelerometer_RawDataPacket(DataPacket packet) : base(ValidPacketTypes.accelerometer_raw_data_packet, numberOfVals, packet)
        {

        }

        public XYZSensor GetAcceleration()
        {
            AndroidSensor sensor = DeSerialize();
            return new XYZSensor(sensor.GetValues());
        }
    }
}
