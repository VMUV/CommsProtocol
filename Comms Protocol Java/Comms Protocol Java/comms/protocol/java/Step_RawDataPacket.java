package comms.protocol.java;

public class Step_RawDataPacket extends AndroidDataPacket {

	private static int numberOfValues = 0;

	public Step_RawDataPacket() {
		super(ValidPacketTypes.step_detector_raw_data_packet, numberOfValues);
	}

	public Step_RawDataPacket(byte[] payload) throws Exception {
		super(ValidPacketTypes.step_detector_raw_data_packet, numberOfValues, payload);
	}

	public Step_RawDataPacket(DataPacket packet) throws Exception {
		super(ValidPacketTypes.step_detector_raw_data_packet, numberOfValues, packet);
	}
}
