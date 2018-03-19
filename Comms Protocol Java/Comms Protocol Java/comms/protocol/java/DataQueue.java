package comms.protocol.java;

import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;

public class DataQueue 
{
	private SimpleQueue<DataPacket> _fifo;
    private int _maxSize;
    
    public DataQueue()
    {
        _maxSize = 64;
        _fifo = new SimpleQueue<DataPacket>(_maxSize);
    }

    public DataQueue(int size)
    {
        _maxSize = size;
        _fifo = new SimpleQueue<DataPacket>(_maxSize);
    }
    
    public int getMaxSize()
    {
    	return _maxSize;
    }
    
    public boolean IsEmpty()
    {
        return (_fifo.size() == 0);
    }
    
    public boolean Add(DataPacket packet)
    {
        boolean rtn = _fifo.size() < _maxSize;
        if (rtn)
            rtn = _fifo.enqueue(packet);

        return rtn;
    }
    
    public DataPacket Get()
    {
        DataPacket rtn = new DataPacket();
        if (_fifo.size() > 0)
        {
            try { rtn = _fifo.dequeue(); }
            catch (NoSuchElementException e) { };
        }

        return rtn;
    }
    
    public int GetStreamable(byte[] stream, int maxSize)
    {
        int numBytesQueued = 0;
        while ((numBytesQueued < maxSize) && (_fifo.size() > 0))
        {
            DataPacket packet = Get();
            int size = packet.getExpectedLen();
            if (size == -1)
                continue;

            size += DataPacket.NumOverHeadBytes;
            if ((maxSize - numBytesQueued) > size)
            {
                stream[numBytesQueued++] = (byte)packet.getPacketType().getValue();
                stream[numBytesQueued++] = (byte)((packet.getExpectedLen()) >> 8);
                stream[numBytesQueued++] = (byte)(packet.getExpectedLen());
                byte[] payload = packet.getPayload();
                for (int i = 0; i < packet.getExpectedLen(); i++)
                    stream[numBytesQueued++] = payload[i];
            }
            else
                break;
        }

        return numBytesQueued;
    }
    
    public void ParseStreamable(byte[] stream, int maxSize)
    {
        int index = 0;
        while ((index < maxSize) && (_fifo.size() < _maxSize))
        {
            ValidPacketTypes type;
            short expectedLen = 0;
            if ((maxSize - index) > DataPacket.NumOverHeadBytes)
            {
                type = ValidPacketTypes.values()[stream[index++]];
                expectedLen = (short)stream[index++];
                expectedLen <<= 8;
                expectedLen |= (short)stream[index++];
            }
            else
                return;

            if ((maxSize - index) > expectedLen)
            {
                byte[] payload = new byte[expectedLen];
                for (int i = 0; i < expectedLen; i++)
                    payload[i] = stream[index++];
                DataPacket packet = new DataPacket();
                packet.setPacketType(type);
                packet.setExpectedLen(expectedLen);
                packet.setPayload(payload);
                Add(packet);
            }
        }
    }
}
