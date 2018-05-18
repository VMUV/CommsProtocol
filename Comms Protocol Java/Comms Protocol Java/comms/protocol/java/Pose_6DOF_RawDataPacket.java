package comms.protocol.java;

public class Pose_6DOF_RawDataPacket extends AndroidDataPacket {
	private static int numberOfValues = 15;
	private Quaternion totalRotation;
	private Quaternion deltaRotation;
	private XYZSensor totalTranslation;
	private XYZSensor deltaTranslation;
	private float sequenceNumber;

	public Pose_6DOF_RawDataPacket() {
		super(ValidPacketTypes.pose_6DOF_raw_data_packet, numberOfValues);
	}

	public Pose_6DOF_RawDataPacket(byte[] payload) throws Exception {
		super(ValidPacketTypes.pose_6DOF_raw_data_packet, numberOfValues, payload);
	}

	public Pose_6DOF_RawDataPacket(DataPacket packet) throws Exception {
		super(ValidPacketTypes.pose_6DOF_raw_data_packet, numberOfValues, packet);
	}

	private void UpdateValues() {
		AndroidSensor sensor = DeSerialize();
		float[] values = sensor.GetValues();
		totalRotation = new Quaternion(values[0], values[1], values[2], values[3]);
		totalTranslation = new XYZSensor(values[4], values[5], values[6]);
		deltaRotation = new Quaternion(values[7], values[8], values[9], values[10]);
		deltaTranslation = new XYZSensor(values[11], values[12], values[13]);
		sequenceNumber = values[14];
	}

	public Quaternion GetTotalRotation() {
		UpdateValues();
		return totalRotation;
	}

	public XYZSensor GetTotalTranslation() {
		UpdateValues();
		return totalTranslation;
	}
	
	public Quaternion GetDeltaRotation() {
		UpdateValues();
		return deltaRotation;
	}
	
	public XYZSensor GetDeltaTranslation() {
		UpdateValues();
		return deltaTranslation;
	}
	
	public float GetSequenceNumber() {
		UpdateValues();
		return sequenceNumber;
	}
}
