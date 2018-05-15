package comms.protocol.java;

import java.nio.ByteBuffer;

public class AndroidSensor {
	private float[] values;
	private long timeStamp;

	public static int NUM_BYTES_PER_LONG = 8;
	public static int NUM_BYTES_PER_FLOAT = 4;

	public AndroidSensor(int numValues) {
		values = new float[numValues];
		timeStamp = 0;
	}

	public AndroidSensor(float[] vals, long tStamp) {
		values = vals;
		timeStamp = tStamp;
	}

	public void SetValues(float[] vals) {
		values = vals;
	}

	public void SetTimeStamp(long tStamp) {
		timeStamp = tStamp;
	}

	public long GetTimeStamp() {
		return timeStamp;
	}

	public float[] GetValues() {
		return values;
	}

	public int Length() {
		return values.length;
	}

	public byte[] GetBytes() {
		ByteBuffer buffer = ByteBuffer.allocate((values.length * NUM_BYTES_PER_FLOAT) + NUM_BYTES_PER_LONG);
		for (int i = 0; i < values.length; i++)
			buffer.putFloat(values[i]);
		buffer.putLong(timeStamp);
		return buffer.array();
	}

	public void SetBytes(byte[] bytes) {
		if (bytes.length < ((values.length * NUM_BYTES_PER_FLOAT) + NUM_BYTES_PER_LONG))
			return;
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		for (int i = 0; i < values.length; i++)
			values[i] = buffer.getFloat();
		timeStamp = buffer.getLong();
	}
}
