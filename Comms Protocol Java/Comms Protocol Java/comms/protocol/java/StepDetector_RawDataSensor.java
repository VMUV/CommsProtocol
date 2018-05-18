package comms.protocol.java;

public class StepDetector_RawDataSensor extends AndroidDataPacket{
	private static int numberOfValues = 1;

	public StepDetector_RawDataSensor() {
		super(ValidPacketTypes.step_detector_raw_data_packet, numberOfValues);
	}

	public StepDetector_RawDataSensor(byte[] payload) throws Exception {
		super(ValidPacketTypes.step_detector_raw_data_packet, numberOfValues, payload);
	}

	public StepDetector_RawDataSensor(DataPacket packet) throws Exception {
		super(ValidPacketTypes.step_detector_raw_data_packet, numberOfValues, packet);
	}
}