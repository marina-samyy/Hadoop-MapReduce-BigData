package mapper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Writable;

public class RatingStatsWritable implements Writable {

    private int sum;
    private int count;
    private int fiveStar;

    public RatingStatsWritable() {}

    public RatingStatsWritable(int sum, int count, int fiveStar) {
        this.sum = sum;
        this.count = count;
        this.fiveStar = fiveStar;
    }
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(sum);
        out.writeInt(count);
        out.writeInt(fiveStar);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        sum = in.readInt();
        count = in.readInt();
        fiveStar = in.readInt();
    }
    public int getSum() {
        return sum;
    }

    public int getCount() {
        return count;
    }

    public int getFiveStar() {
        return fiveStar;
    }
    public void add(RatingStatsWritable other) {
        this.sum += other.sum;
        this.count += other.count;
        this.fiveStar += other.fiveStar;
    }
}
