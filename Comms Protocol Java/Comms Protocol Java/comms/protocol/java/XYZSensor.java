package comms.protocol.java;

public class XYZSensor {
	public float x;
	public float y;
	public float z;

	public XYZSensor() {
		x = 0;
		y = 0;
		z = 0;
	}

	public XYZSensor(float[] vals) {
		x = vals[0];
		y = vals[1];
		z = vals[2];
	}

	public XYZSensor(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
