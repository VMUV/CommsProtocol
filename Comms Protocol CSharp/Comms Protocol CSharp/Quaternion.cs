
namespace Comms_Protocol_CSharp
{
    public class Quaternion
    {
        public float x;
        public float y;
        public float z;
        public float w;

        public Quaternion()
        {
            x = 0;
            y = 0;
            z = 0;
            w = 0;
        }

        public Quaternion(float[] vals)
        {
            x = vals[0];
            y = vals[1];
            z = vals[2];
            w = vals[3];
        }

        public Quaternion(float x, float y, float z, float w)
        {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }
    }
}
