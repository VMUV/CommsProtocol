package comms.protocol.java;

public class AndroidDataPacket extends DataPacket {
	private static int numVals;
	private static int totalLen;

	public AndroidDataPacket(ValidPacketTypes type, int numberOfValues) {
		super(type, (short) ((AndroidSensor.NUM_BYTES_PER_FLOAT * numberOfValues) + AndroidSensor.NUM_BYTES_PER_LONG),
				new byte[(AndroidSensor.NUM_BYTES_PER_FLOAT * numberOfValues) + AndroidSensor.NUM_BYTES_PER_LONG]);
		numVals = numberOfValues;
		totalLen = (AndroidSensor.NUM_BYTES_PER_FLOAT * numVals) + AndroidSensor.NUM_BYTES_PER_LONG;
	}

	public AndroidDataPacket(ValidPacketTypes type, int numberOfValues, byte[] payload) throws Exception {
		super(type, (short) ((AndroidSensor.NUM_BYTES_PER_FLOAT * numberOfValues) + AndroidSensor.NUM_BYTES_PER_LONG),
				new byte[(AndroidSensor.NUM_BYTES_PER_FLOAT * numberOfValues) + AndroidSensor.NUM_BYTES_PER_LONG]);
		numVals = numberOfValues;
		totalLen = (AndroidSensor.NUM_BYTES_PER_FLOAT * numVals) + AndroidSensor.NUM_BYTES_PER_LONG;
		Serialize(payload);
	}

	public AndroidDataPacket(ValidPacketTypes type, int numberOfValues, DataPacket packet) throws Exception {
		numVals = numberOfValues;
		totalLen = (AndroidSensor.NUM_BYTES_PER_FLOAT * numVals) + AndroidSensor.NUM_BYTES_PER_LONG;
		if ((packet.getPacketType() != type) || (packet.getExpectedLen() != totalLen)) {
			this.setPacketType(type);
			this.setExpectedLen((short) totalLen);
			this.setPayload(new byte[this.getExpectedLen()]);
		} else {
			this.setPacketType(packet.getPacketType());
			this.setExpectedLen(packet.getExpectedLen());
			Serialize(packet.getPayload());
		}
	}

	public void Serialize(byte[] payload) throws Exception {
		if (payload.length < this.getExpectedLen())
			throw new Exception();

		this.setPayload(payload);
	}

	public void Serialize(AndroidSensor sensor) throws Exception {
		if (sensor.Length() < numVals)
			throw new Exception();

		this.setPayload(sensor.GetBytes());
	}

	public AndroidSensor DeSerialize() {
		AndroidSensor sensor = new AndroidSensor(numVals);
		sensor.SetBytes(this.getPayload());
		return sensor;
	}
}
