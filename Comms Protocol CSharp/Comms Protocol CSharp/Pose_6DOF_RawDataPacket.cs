
namespace Comms_Protocol_CSharp
{
    public class Pose_6DOF_RawDataPacket : AndroidDataPacket
    {
        private static int numberOfVals = 15;
        private Quaternion totalRotation = new Quaternion();
        private Quaternion deltaRotation = new Quaternion();
        private XYZSensor totalTranslation = new XYZSensor();
        private XYZSensor deltaTranslation = new XYZSensor();
        private float sequenceNumber = 0;

        public Pose_6DOF_RawDataPacket() : base(ValidPacketTypes.pose_6DOF_raw_data_packet, numberOfVals)
        {

        }

        public Pose_6DOF_RawDataPacket(byte[] payload) : base(ValidPacketTypes.pose_6DOF_raw_data_packet, numberOfVals, payload)
        {

        }

        public Pose_6DOF_RawDataPacket(DataPacket packet) : base(ValidPacketTypes.pose_6DOF_raw_data_packet, numberOfVals, packet)
        {

        }

        private void UpdateValues()
        {
            AndroidSensor sensor = DeSerialize();
            float[] values = sensor.GetValues();
            if (values.Length >= numberOfVals)
            {
                totalRotation = new Quaternion(values[0], values[1], values[2], values[3]);
                totalTranslation = new XYZSensor(values[4], values[5], values[6]);
                deltaRotation = new Quaternion(values[7], values[8], values[9], values[10]);
                deltaTranslation = new XYZSensor(values[11], values[12], values[13]);
                sequenceNumber = values[14];
            }
        }

        public Quaternion GetTotalRotation()
        {
            UpdateValues();
            return totalRotation;
        }

        public XYZSensor GetTotalTranslation()
        {
            UpdateValues();
            return totalTranslation;
        }

        public Quaternion GetDeltaRotation()
        {
            UpdateValues();
            return deltaRotation;
        }

        public XYZSensor GetDeltaTranslation()
        {
            UpdateValues();
            return deltaTranslation;
        }

        public float GetSequenceNumber()
        {
            UpdateValues();
            return sequenceNumber;
        }
    }
}
