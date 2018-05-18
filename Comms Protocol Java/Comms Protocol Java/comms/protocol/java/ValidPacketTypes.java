package comms.protocol.java;

public enum ValidPacketTypes {
	test_packet(0), motus_1_raw_data_packet(1), rotation_vector_raw_data_packet(2), accelerometer_raw_data_packet(
			3), linear_acceleration_raw_data_packet(4), gyro_raw_data_packet(
					5), pose_6DOF_raw_data_packet(6), step_detector_raw_data_packet(7), end_valid_packet_types(8);

	private final int value;

	private ValidPacketTypes(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	// will add more packet types when we start using the gyroscope/other hardware
	// on the android device
}
