using System;

namespace Comms_Protocol_CSharp
{
    public class AndroidDataPacket : DataPacket
    {
        private static int numVals;
        private static int totalLen;

        public AndroidDataPacket(ValidPacketTypes type, int numberOfValues)
        {
            Type = type;
            ExpectedLen = (short)((AndroidSensor.NUM_BYTES_PER_FLOAT * numberOfValues) + AndroidSensor.NUM_BYTES_PER_LONG);
            Payload = new byte[(AndroidSensor.NUM_BYTES_PER_FLOAT * numberOfValues) + AndroidSensor.NUM_BYTES_PER_LONG];
            numVals = numberOfValues;
            totalLen = (AndroidSensor.NUM_BYTES_PER_FLOAT * numVals) + AndroidSensor.NUM_BYTES_PER_LONG;
        }

        public AndroidDataPacket(ValidPacketTypes type, int numberOfValues, byte[] payload)
        {
            Type = type;
            ExpectedLen = (short)((AndroidSensor.NUM_BYTES_PER_FLOAT * numberOfValues) + AndroidSensor.NUM_BYTES_PER_LONG);
            numVals = numberOfValues;
            totalLen = (AndroidSensor.NUM_BYTES_PER_FLOAT * numVals) + AndroidSensor.NUM_BYTES_PER_LONG;
            Serialize(payload);
        }

        public AndroidDataPacket(ValidPacketTypes type, int numberOfValues, DataPacket packet)
        {
            numVals = numberOfValues;
            totalLen = (AndroidSensor.NUM_BYTES_PER_FLOAT * numVals) + AndroidSensor.NUM_BYTES_PER_LONG;
            if ((packet.Type != type) || (packet.ExpectedLen != totalLen))
            {
                this.Type = type;
                this.ExpectedLen = (short)totalLen;
                this.Payload = new byte[this.ExpectedLen];
            }
            else
            {
                this.Type = packet.Type;
                this.ExpectedLen = packet.ExpectedLen;
                Serialize(packet.Payload);
            }
        }

        public void Serialize(byte[] payload)
        {
            if (payload.Length < this.ExpectedLen)
                return;

            this.Payload = payload;
        }

        public void Serialize(AndroidSensor sensor)
        {
            if (sensor.Length() < numVals)
                throw new Exception();

            this.Payload = sensor.GetBytes();
        }

        public AndroidSensor DeSerialize()
        {
            AndroidSensor sensor = new AndroidSensor(numVals);
            sensor.SetBytes(this.Payload);
            return sensor;
        }
    }
}
