
namespace Comms_Protocol_CSharp
{
    class StepDetector_RawDataPacket : AndroidDataPacket
    {
        private static int numberOfVals = 1;

        public StepDetector_RawDataPacket() : base(ValidPacketTypes.step_detector_raw_data_packet, numberOfVals)
        {

        }

        public StepDetector_RawDataPacket(byte[] payload) : base(ValidPacketTypes.step_detector_raw_data_packet, numberOfVals, payload)
        {

        }

        public StepDetector_RawDataPacket(DataPacket packet) : base(ValidPacketTypes.step_detector_raw_data_packet, numberOfVals, packet)
        {

        }
    }
}
