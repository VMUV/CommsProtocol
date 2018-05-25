package comms.protocol.java.tests;

import static org.junit.Assert.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.junit.Test;
import comms.protocol.java.AndroidSensor;

public class AndroidSensorUnitTests {

	private float[] dummyVals = { 0.35f, 0.669f, 45f, 22f, 6634.345f };
	private long dummyTimeStamp = 11216541;

	@Test
	public void TestConstructor() {
		AndroidSensor sensor1 = new AndroidSensor(10);
		assertEquals(10, sensor1.Length());
		assertEquals(0, sensor1.GetTimeStamp());

		float[] vals = { (float) 7.6, (float) -45.0, (float) 0.00025, 2 };
		long timeStamp = 1021964813;
		AndroidSensor sensor2 = new AndroidSensor(vals, timeStamp);
		assertArrayEquals(vals, sensor2.GetValues(), 0.001f);
		assertEquals(timeStamp, sensor2.GetTimeStamp());
		assertEquals(vals.length, sensor2.Length());
	}

	@Test
	public void TestValues() {
		AndroidSensor sensor1 = new AndroidSensor(dummyVals.length);
		sensor1.SetValues(dummyVals);
		sensor1.SetTimeStamp(dummyTimeStamp);
		assertArrayEquals(dummyVals, sensor1.GetValues(), 0.001f);
		assertEquals(dummyTimeStamp, sensor1.GetTimeStamp());
	}

	@Test
	public void TestBytes() {
		ByteBuffer buffer = ByteBuffer
				.allocate((dummyVals.length * AndroidSensor.NUM_BYTES_PER_FLOAT) + AndroidSensor.NUM_BYTES_PER_LONG);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		for (int i = 0; i < dummyVals.length; i++)
			buffer.putFloat(dummyVals[i]);
		buffer.putLong(dummyTimeStamp);

		AndroidSensor sensor1 = new AndroidSensor(dummyVals.length);
		sensor1.SetBytes(buffer.array());

		assertArrayEquals(dummyVals, sensor1.GetValues(), 0.001f);
		assertEquals(dummyTimeStamp, sensor1.GetTimeStamp());
	}
	
	@Test
	public void TestBytesInvalid() {
		byte[] bytes = new byte[1];
		AndroidSensor sensor1 = new AndroidSensor(dummyVals.length);
		sensor1.SetBytes(bytes);

		assertEquals(0, sensor1.GetTimeStamp());
	}
}
